package com.sublimee.boot.locks.repository.card;

import com.sublimee.boot.locks.model.card.Card;

import javax.persistence.EntityManager;
import java.util.function.Consumer;


public interface CardRepository {

    void persist(Card item);

    void executeInTransaction(Consumer<EntityManager> consumer);

}
