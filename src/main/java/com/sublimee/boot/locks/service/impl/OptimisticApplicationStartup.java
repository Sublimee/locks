//package com.sublimee.boot.locks.service;
//
//import com.sublimee.boot.locks.model.card.VersionedCard;
//import com.sublimee.boot.locks.repository.card.CardRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.ApplicationListener;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//
//import javax.persistence.EntityManagerFactory;
//import javax.persistence.PersistenceUnit;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.TimeUnit;
//
//@Component
//public class OptimisticApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {
//
//    private final CardRepository cardRepository;
//
//    @PersistenceUnit(unitName = "cards")
//    private EntityManagerFactory emf;
//
//    @Autowired
//    public OptimisticApplicationStartup(CardRepository cardRepository) {
//        this.cardRepository = cardRepository;
//    }
//
//
//    @Override
//    public void onApplicationEvent(final ApplicationReadyEvent event) {
//        ExecutorService es = Executors.newFixedThreadPool(2);
//        try {
//            cardRepository.persist(new VersionedCard());
//
//            es.execute(() -> cardRepository.executeInTransaction(entityManager -> {
//                List<VersionedCard> cards = entityManager.createQuery("select c from VersionedCard c", VersionedCard.class)
//                        .getResultList();
//                es.execute(() -> cardRepository.executeInTransaction(entityManager2 -> {
//                    List<VersionedCard> cards2 = entityManager2.createQuery("select c from VersionedCard c", VersionedCard.class)
//                            .getResultList();
//                    cards2.get(0).setBalance(1000);
//                }));
//                try {
//                    TimeUnit.SECONDS.sleep(2);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                cards.get(0).setBalance(1000);
//            }));
//
//            es.awaitTermination(1, TimeUnit.MINUTES);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } finally {
//            emf.close();
//        }
//    }
//
//}
