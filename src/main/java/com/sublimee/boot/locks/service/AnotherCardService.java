package com.sublimee.boot.locks.service;

import com.sublimee.boot.locks.model.card.UnversionedCard;
import com.sublimee.boot.locks.repository.CardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AnotherCardService {

    private final CardRepository cardRepository;

    public AnotherCardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Transactional
    public Optional<UnversionedCard> selectForUpdateNoWait(UUID id) {
        Optional<UnversionedCard> byId = cardRepository.findById(id);
        log.info(String.valueOf(byId.get().getBalance()));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return byId;
    }

}
