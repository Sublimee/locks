package com.sublimee.boot.locks.repository.card;

import com.sublimee.boot.locks.model.card.Card;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceUnit;
import java.util.function.Consumer;

@Repository
public class CardRepositoryImpl implements CardRepository {

    @PersistenceUnit(unitName = "cards")
    private EntityManagerFactory emf;

    @Override
    public void persist(Card item) {
        executeInTransaction(entityManager -> entityManager.persist(item));
    }

    @Override
    public void executeInTransaction(Consumer<EntityManager> consumer) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        consumer.accept(entityManager);

        transaction.commit();
        entityManager.close();
    }

}
