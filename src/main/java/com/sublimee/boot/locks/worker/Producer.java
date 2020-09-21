package com.sublimee.boot.locks.worker;

import com.sublimee.boot.locks.model.card.VersionedCard;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Producer implements Runnable {

    private final BlockingQueue<VersionedCard> queue;

    private final Random random;

    public Producer(BlockingQueue<VersionedCard> queue) {
        this.queue = queue;
        this.random = new Random();
    }

    @Override
    public void run() {
        process();
    }

    private void process() {
        for (int i = 0; i < 2500; i++) {
            VersionedCard versionedCard = new VersionedCard();
            versionedCard.setBalance(random.nextInt());
            try {
                queue.put(versionedCard);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}