package com.taotao.cloud.java.thread.pool;

import java.util.Random;

public class TaskRunnable implements Runnable {
    private int s;

    public TaskRunnable(int s) {
        this.s = s;
    }

    Random r = new Random();

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        long currentTimeMillis = System.currentTimeMillis();
        System.out.println(name + " 启动时间：" + currentTimeMillis / 1000);

        int rint = r.nextInt(3);
        try {
            Thread.sleep(rint * 1000);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        System.out.println(name + " is working..." + s);

    }

}
