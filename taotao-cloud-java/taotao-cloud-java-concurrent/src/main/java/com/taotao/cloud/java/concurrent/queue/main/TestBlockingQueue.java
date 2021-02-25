package com.taotao.cloud.java.queue.main;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TestBlockingQueue {

    public static void main(String[] args) {


        BlockingQueue<String> queue = new LinkedBlockingQueue<String>(2);
        // BlockingQueue<String> queue = new LinkedBlockingQueue<String>();
        // 不设置的话，LinkedBlockingQueue默认大小为Integer.MAX_VALUE
        // BlockingQueue<String> queue = new ArrayBlockingQueue<String>(2);
        TestBlockingQueueConsumer consumer = new TestBlockingQueueConsumer(queue);
        TestBlockingQueueProducer producer = new TestBlockingQueueProducer(queue);
        for (int i = 0; i < 3; i++) {
            new Thread(producer, "Producer" + (i + 1)).start();
        }
        for (int i = 0; i < 5; i++) {
            new Thread(consumer, "Consumer" + (i + 1)).start();
        }

        new Thread(producer, "Producer" + (5)).start();


    }


}
