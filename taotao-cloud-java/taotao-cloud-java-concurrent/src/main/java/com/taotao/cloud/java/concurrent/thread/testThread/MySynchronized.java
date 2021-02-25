package com.taotao.cloud.java.thread.testThread;

public class MySynchronized {
    public static void main(String[] args) {
        final MySynchronized mySynchronized = new MySynchronized();
        final MySynchronized mySynchronized2 = new MySynchronized();

        new Thread("thread1") {
            @Override
            public void run() {
                synchronized (mySynchronized) {
                    try {
                        System.out.println(this.getName() + " start");
//                        int i = 1 / 0;   //如果发生异常，jvm会将锁释放
                        Thread.sleep(5000);
                        System.out.println(this.getName() + "醒了");
                        System.out.println(this.getName() + " end");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        new Thread("thread2") {
            @Override
            public void run() {
                synchronized (mySynchronized) {         //争抢同一把锁时，线程1没释放之前，线程2只能等待
//					synchronized (mySynchronized2) {    //如果不是一把锁，可以看到两句话同时打印
                    System.out.println(this.getName() + " start");
                    System.out.println(this.getName() + " end");

                }
            }
        }.start();
    }
}
