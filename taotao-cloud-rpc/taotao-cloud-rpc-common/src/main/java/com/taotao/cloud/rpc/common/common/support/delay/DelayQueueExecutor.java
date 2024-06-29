package com.taotao.cloud.rpc.common.common.support.delay;


import java.util.concurrent.DelayQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 基于延迟队列的延迟实现
 * @since 0.1.7
 */
public class DelayQueueExecutor implements DelayExecutor {

    /**
     * 日志信息
     *
     * @since 0.1.7
     */
    private static final Logger LOG = LoggerFactory.getLogger(DelayQueueExecutor.class);

    private final DelayQueue<DelayElem> delayQueue;

    public DelayQueueExecutor() {
        delayQueue = new DelayQueue<>();

        // 读取线程提前开始
        ReadThread readThread = new ReadThread(delayQueue);
        readThread.start();
    }

    @Override
    public void delay(long delayInMills, Runnable runnable) {
//        log.info("开始添加延迟 {}ms 的可运行对象", delayInMills);
        DelayElem delayElem = new DelayElem(delayInMills, runnable);
        delayQueue.add(delayElem);
//        log.info("完成添加延迟 {}ms 的可运行对象", delayInMills);
    }

    /**
     * 读取线程
     * @since 0.1.7
     */
    private static class ReadThread extends Thread {
        private final DelayQueue<DelayElem> delayQueue;
        private ReadThread(DelayQueue<DelayElem> delayQueue) {
            this.delayQueue = delayQueue;
        }

        @Override
        public void run() {
            while (true){
                try {
                    DelayElem element = delayQueue.take();
                    long delayInMills = element.delayMills();
//                    log.info("开始获取延迟 {}ms 的可运行对象", delayInMills);
                    Runnable runnable = element.msg();
                    runnable.run();
//                    log.info("完成获取延迟 {}ms 的可运行对象", delayInMills);
                } catch (InterruptedException e) {
                    //log.error("延迟任务执行遇到异常", e);
                }
            }
        }
    }

}
