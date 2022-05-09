package com.taotao.cloud.health.alarm.common.concurrent.forkjoin;

public class ForkJoinPoolFactory {

    private int parallelism;

    private ExtendForkJoinPool forkJoinPool;

    public ForkJoinPoolFactory() {
        this(Runtime.getRuntime().availableProcessors() * 16);
    }

    public ForkJoinPoolFactory(int parallelism) {
        this.parallelism = parallelism;
        forkJoinPool = new ExtendForkJoinPool(parallelism);
    }

    public ExtendForkJoinPool getObject() {
        return this.forkJoinPool;
    }

    public int getParallelism() {
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }


    public void destroy() throws Exception {
        this.forkJoinPool.shutdown();
    }


}
