package com.sublimee.boot.locks.service.impl;

import com.sublimee.boot.locks.model.card.VersionedCard;
import com.sublimee.boot.locks.repository.card.CardRepository;
import com.sublimee.boot.locks.service.listener.CacheEventLogger;
import com.sublimee.boot.locks.worker.Consumer;
import com.sublimee.boot.locks.worker.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class CardServiceImpl implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CardServiceImpl.class);

    private final CardRepository cardRepository;

    private Integer nThreads = 2;

    @Autowired
    public CardServiceImpl(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }


    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        BlockingQueue<VersionedCard> cardsToPersistQueue = new LinkedBlockingQueue<>(1000);
        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        if (nThreads <= 1) {
            throw new IllegalArgumentException("Can not create producer due to small threads capacity:" + nThreads);
        }
        executorService.submit(new Producer(cardsToPersistQueue));
        for (int i = 1; i < nThreads; i++) {
            executorService.submit(new Consumer(cardsToPersistQueue, cardRepository));
        }


        new Thread(() -> {
            while (true) {
                LOGGER.info("Queue size is: " + cardsToPersistQueue.size());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            while (true) {
                LOGGER.info("Cards in DB: " + cardRepository.count());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

}
