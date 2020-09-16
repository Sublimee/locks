//package com.sublimee.boot.locks.service;
//
//import com.sublimee.boot.locks.model.card.Card;
//import com.sublimee.boot.locks.repository.card.CardRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.LockModeType;
//import javax.persistence.PersistenceUnit;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class PessimisticApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
//
//    private final CardRepository cardRepository;
//
//    @PersistenceUnit(unitName = "cards")
//    private EntityManagerFactory emf;
//
//    @Autowired
//    public PessimisticApplicationStartup(CardRepository cardRepository) {
//        this.cardRepository = cardRepository;
//    }
//
//
//    @Override
//    public void onApplicationEvent(final ApplicationReadyEvent event) {
//        ExecutorService es = Executors.newFixedThreadPool(2);
//        try {
//            cardRepository.persist(new Card());
//
//            es.execute(() -> cardRepository.executeInTransaction(entityManager1 -> {
//                List<Card> cards = entityManager1.createQuery("select c from Card c", Card.class)
//                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
//                        .getResultList();
//                try {
//                    TimeUnit.SECONDS.sleep(10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }));
//            es.execute(() -> cardRepository.executeInTransaction(entityManager -> {
//                try {
//                    TimeUnit.SECONDS.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                List<Card> cards = entityManager.createQuery("select c from Card c", Card.class)
//                        .setLockMode(LockModeType.PESSIMISTIC_WRITE)
//                        .setHint("javax.persistence.lock.timeout", 0)
//                        .getResultList();
//                cards.get(0).setBalance(1000);
//            }));
//
//            es.shutdown();
//            es.awaitTermination(1, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            emf.close();
//        }
//    }
//
//}
