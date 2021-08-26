package com.taotao.cloud.seckill.biz.queue.delay.jvm;

import java.util.concurrent.DelayQueue;

/**
 * 红包延迟队列
 */
public class RedPacketQueue {

    /** 用于多线程间下单的队列 */
    private static DelayQueue<RedPacketMessage> queue = new DelayQueue<>();

    /**
     * 私有的默认构造子，保证外界无法直接实例化
     */
    private RedPacketQueue(){}
    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder{
        /**
         * 静态初始化器，由JVM来保证线程安全
         */
        private  static RedPacketQueue queue = new RedPacketQueue();
    }
    //单例队列
    public static RedPacketQueue getQueue(){
        return SingletonHolder.queue;
    }
    /**
     * 生产入队
     * 1、执行加锁操作
     * 2、把元素添加到优先级队列中
     * 3、查看元素是否为队首
     * 4、如果是队首的话，设置leader为空，唤醒所有等待的队列
     * 5、释放锁
     */
    public  Boolean  produce(RedPacketMessage message){
        return queue.add(message);
    }
    /**
     * 消费出队
     * 1、执行加锁操作
     * 2、取出优先级队列元素q的队首
     * 3、如果元素q的队首/队列为空,阻塞请求
     * 4、如果元素q的队首(first)不为空,获得这个元素的delay时间值
     * 5、如果first的延迟delay时间值为0的话,说明该元素已经到了可以使用的时间,调用poll方法弹出该元素,跳出方法
     * 6、如果first的延迟delay时间值不为0的话,释放元素first的引用,避免内存泄露
     * 7、判断leader元素是否为空,不为空的话阻塞当前线程
     * 8、如果leader元素为空的话,把当前线程赋值给leader元素,然后阻塞delay的时间,即等待队首到达可以出队的时间,在finally块中释放leader元素的引用
     * 9、循环执行从1~8的步骤
     * 10、如果leader为空并且优先级队列不为空的情况下(判断还有没有其他后续节点),调用signal通知其他的线程
     * 11、执行解锁操作
     */
    public  RedPacketMessage consume() throws InterruptedException {
        return queue.take();
    }
}
