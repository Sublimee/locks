package com.sublimee.boot.locks.repository.card;

import com.sublimee.boot.locks.model.card.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, Integer> {

//     void persist(Order item);

//     Order executeInTransaction(Function<EntityManager, Order> function);

}
