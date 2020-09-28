package com.sublimee.boot.locks.service;

import com.sublimee.boot.locks.model.card.UnversionedCard;
import com.sublimee.boot.locks.repository.CardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class CardService implements ApplicationListener<ApplicationReadyEvent> {

    private final CardRepository cardRepository;

    private final AnotherCardService anotherCardService;

    public CardService(CardRepository cardRepository, AnotherCardService anotherCardService) {
        this.cardRepository = cardRepository;
        this.anotherCardService = anotherCardService;
    }

    private static UUID uuid;

    @PostConstruct
    public void init() {
        UnversionedCard versionedCard = new UnversionedCard();
        versionedCard.setBalance(1000);
        uuid = cardRepository.save(versionedCard).getId();
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ExecutorService executorService = Executors.newFixedThreadPool(12);
        List<Callable<UnversionedCard>> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add((Callable) () -> anotherCardService.selectForUpdateNoWait(uuid));
        }

        try {
            executorService.invokeAll(list);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
