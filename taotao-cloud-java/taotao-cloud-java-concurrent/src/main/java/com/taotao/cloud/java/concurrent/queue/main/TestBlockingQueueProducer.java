package com.taotao.cloud.java.queue.main;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class TestBlockingQueueProducer implements Runnable {
    BlockingQueue<String> queue;
    Random random = new Random();

    public TestBlockingQueueProducer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(random.nextInt(10));
                String task = Thread.currentThread().getName() + " made a product " + i;

                System.out.println(task);
                queue.put(task);
            } catch (InterruptedException e) {

                e.printStackTrace();
            }

        }

    }

}
