package com.sublimee.boot.locks.repository.card;

import com.sublimee.boot.locks.model.card.Card;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.function.Consumer;

@Repository
public class CardRepositoryImpl {

    @PersistenceUnit(unitName = "cards")
    private EntityManagerFactory emf;


    public void persist(Card item) {
        executeInTransaction(entityManager -> entityManager.persist(item));
    }

    public Integer count() {
        EntityManager entityManager = emf.createEntityManager();
        Query query = entityManager.createNativeQuery("select count(*) from versioned_card");
        Integer result = (Integer) query.getSingleResult();
        entityManager.close();
        return result;
    }

    public void executeInTransaction(Consumer<EntityManager> consumer) {
        EntityManager entityManager = emf.createEntityManager();
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        consumer.accept(entityManager);

        transaction.commit();
        entityManager.close();
    }

}
