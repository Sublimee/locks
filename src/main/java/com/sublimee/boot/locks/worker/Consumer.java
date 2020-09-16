package com.sublimee.boot.locks.worker;

import com.sublimee.boot.locks.model.card.VersionedCard;
import com.sublimee.boot.locks.repository.card.CardRepository;

import java.util.concurrent.BlockingQueue;


public class Consumer implements Runnable {

    private final CardRepository cardRepository;

    private final BlockingQueue<VersionedCard> queue;

    public Consumer(BlockingQueue<VersionedCard> queue, CardRepository cardRepository) {
        this.cardRepository = cardRepository;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                VersionedCard card = queue.take();
                process(card);
            }
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

    private void process(VersionedCard card) {
        cardRepository.persist(card);
    }

}
