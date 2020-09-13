package com.mymaldonado.sample.boot;

import com.sublimee.boot.locks.Application;
import com.sublimee.boot.locks.model.card.Card;
import com.sublimee.boot.locks.repository.card.CardRepository;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
public class ApplicationTests {

    @Autowired
    private CardRepository cardRepository;


    @Test
    public void selectForUpdate() {
        ExecutorService es = Executors.newFixedThreadPool(2);
        try {
            cardRepository.persist(new Card());

            es.execute(() -> cardRepository.executeInTransaction(entityManager -> {
                List<Card> cards = entityManager.createQuery("select c from Card c", Card.class)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .getResultList();
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
            es.execute(() -> cardRepository.executeInTransaction(entityManager -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<Card> cards = entityManager.createQuery("select c from Card c", Card.class)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
						.setHint("javax.persistence.lock.timeout", 100)
                        .getResultList();
                cards.get(0).setBalance(1000);
            }));

            es.shutdown();
            es.awaitTermination(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void selectForUpdateNoWait() {
        ExecutorService es = Executors.newFixedThreadPool(2);
        try {
            cardRepository.persist(new Card());

            es.execute(() -> cardRepository.executeInTransaction(entityManager -> {
                List<Card> cards = entityManager.createQuery("select c from Card c", Card.class)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .getResultList();
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }));
            es.execute(() -> cardRepository.executeInTransaction(entityManager -> {
                try {
                    TimeUnit.SECONDS.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                List<Card> cards = entityManager.createQuery("select c from Card c", Card.class)
                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                        .setHint("javax.persistence.lock.timeout", 0)
                        .getResultList();
                cards.get(0).setBalance(1000);
            }));

            es.shutdown();
            es.awaitTermination(1, TimeUnit.MINUTES);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}