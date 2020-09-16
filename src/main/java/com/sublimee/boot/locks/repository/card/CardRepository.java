package com.sublimee.boot.locks.repository.card;

import com.sublimee.boot.locks.model.card.Card;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.UUID;
import java.util.function.Consumer;


public interface CardRepository {

    void persist(Card item);

    BigInteger count();

    Card find(UUID id);

    void executeInTransaction(Consumer<EntityManager> consumer);

}
