package com.sublimee.boot.locks.service.impl;

import com.sublimee.boot.locks.model.card.VersionedCard;
import com.sublimee.boot.locks.repository.card.CardRepositoryImpl;
import com.sublimee.boot.locks.worker.Consumer;
import com.sublimee.boot.locks.worker.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class CardServiceImpl implements ApplicationListener<ApplicationReadyEvent> {

    private final CardRepositoryImpl cardRepository;
    private Integer nThreads = 5;
    private volatile BlockingQueue<VersionedCard> cardsToPersistQueue = new LinkedBlockingQueue<>(200);
    @Autowired
    public CardServiceImpl(CardRepositoryImpl cardRepository) {
        this.cardRepository = cardRepository;
    }

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        ExecutorService executorService = Executors.newFixedThreadPool(nThreads);
        if (nThreads <= 3) {
            throw new IllegalArgumentException("Can not create producer due to small threads capacity:" + nThreads);
        }

        executorService.submit(new Producer(cardsToPersistQueue));
        for (int i = 3; i < nThreads; i++) {
            executorService.submit(new Consumer(cardsToPersistQueue, cardRepository));
        }

        executorService.submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                System.out.println("Queue size is: " + cardsToPersistQueue.size());
                System.out.println("Cards in DB: " + cardRepository.count());
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        shutdownAndAwaitTermination(executorService);
    }

    void shutdownAndAwaitTermination(ExecutorService pool) {
        pool.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(60, TimeUnit.SECONDS)) {
                pool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(60, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
    }

}
