package com.sublimee.boot.locks.worker;

import com.sublimee.boot.locks.model.card.VersionedCard;
import com.sublimee.boot.locks.repository.card.CardRepositoryImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

@Slf4j
public class Consumer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Consumer.class);

    private final CardRepositoryImpl cardRepository;
    private final BlockingQueue<VersionedCard> queue;

    public Consumer(BlockingQueue<VersionedCard> queue, CardRepositoryImpl cardRepository) {
        this.cardRepository = cardRepository;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                VersionedCard card = queue.take();
                process(card);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process(VersionedCard card) {
        cardRepository.persist(card);
    }

}
