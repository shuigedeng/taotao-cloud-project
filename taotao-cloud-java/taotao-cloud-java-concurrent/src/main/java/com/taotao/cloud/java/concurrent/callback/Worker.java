package com.taotao.cloud.java.concurrent.callback;

public class Worker {
    /**
     * Fetcher.fetchData()方法需传递一个FetcherCallback类型的参数，当获得数据或发生错误时被回调。对于每种情况都提供了同意的方法：
     * •	FetcherCallback.onData()，将接收数据时被调用
     * •	FetcherCallback.onError()，发生错误时被调用
     * 因为可以将这些方法的执行从"caller"线程移动到其他的线程执行；但也不会保证FetcherCallback的每个方法都会被执行。回调过程有个问题就是当你使用链式调用
     * 很多不同的方法会导致线性代码；有些人认为这种链式调用方法会导致代码难以阅读，但是我认为这是一种风格和习惯问题。例如，基于Javascript的Node.js越来越受欢迎，它使用了大量的回调，许多人都认为它的这种方式利于阅读和编写。
     */

    public void doWork() {
        System.out.println("调用doWork方法");
        Fetcher fetcher = new MyFetcher(new Data(1, 2));
        fetcher.fetchData(new FetcherCallback() {
            @Override
            public void onError(Throwable cause) {
                System.out.println("错误回调: " + cause.getMessage());
            }

            @Override
            public void onData(Data data) {
                System.out.println("正常回调: " + data.toString());
            }
        });
    }

    public static void main(String[] args) {
        Worker w = new Worker();
        w.doWork();
    }

}
