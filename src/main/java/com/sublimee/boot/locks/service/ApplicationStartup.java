package com.sublimee.boot.locks.service;

import com.sublimee.boot.locks.model.card.Card;
import com.sublimee.boot.locks.repository.card.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.persistence.LockModeType;
import java.util.List;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    private final CardRepository cardRepository;

    @Autowired
    public ApplicationStartup(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        System.out.println("Ready to start");
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        cardRepository.persist(new Card());

        cardRepository.executeInTransaction(entityManager -> {
            List<Card> cards = entityManager.createQuery("select c from Card c", Card.class)
                    .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                    .getResultList();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("there are " + cards.size() + " cards");
        });
    }

}
