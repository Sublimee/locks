package com.sublimee.boot.locks.service;

import com.sublimee.boot.locks.model.card.Card;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;

@Component
public class ApplicationStartup implements ApplicationListener<ApplicationReadyEvent> {

    @PersistenceUnit(unitName = "cards")
    private EntityManagerFactory emf;

    @EventListener
    public void onApplicationReadyEvent(ApplicationReadyEvent event) {
        System.out.println("Hello");
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        System.out.println("Before!");
        show();
        System.out.println("After!");
    }

    public void show() {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();
        Card card = new Card();
        entityManager.persist(card);
        transaction.commit();
        entityManager.close();
    }

}