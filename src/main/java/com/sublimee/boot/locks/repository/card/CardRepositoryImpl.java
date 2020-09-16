package com.sublimee.boot.locks.repository.card;

import com.sublimee.boot.locks.model.card.Card;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.util.UUID;
import java.util.function.Consumer;

@Repository
public class CardRepositoryImpl implements CardRepository {

    @PersistenceUnit(unitName = "cards")
    private EntityManagerFactory emf;

    private EntityManager entityManager;

    @PostConstruct
    public void init(){
        this.entityManager = emf.createEntityManager();
    }

    @Override
    public void persist(Card item) {
        executeInTransaction(entityManager -> entityManager.persist(item));
    }

    @Override
    public Integer count() {
        Query query = entityManager.createQuery("select count(*) from VersionedCard");
        return (Integer) query.getSingleResult();
    }

    @Override
    public Card find(UUID id) {
        EntityManager entityManager = emf.createEntityManager();
        return entityManager.find(Card.class, id);
    }

    @Override
    public void executeInTransaction(Consumer<EntityManager> consumer) {
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        consumer.accept(entityManager);

        transaction.commit();
        entityManager.close();
    }

}
