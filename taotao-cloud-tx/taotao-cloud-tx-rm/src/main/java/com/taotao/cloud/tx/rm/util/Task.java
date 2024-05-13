package com.taotao.cloud.tx.rm.util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 子事务的等待队列：基于此实现事务控制权
public class Task {
    // 通过ReentrantLock的Condition条件等待队列实现线程阻塞/唤醒
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    // 阻塞挂起线程的方法
    public void waitTask(){
        System.out.println("事务控制权已经被拦截挂起........");
        lock.lock();
        try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    // 唤醒放下线程的方法
    public void signalTask(){
        System.out.println("事务控制权已经被拦截放下........");
        lock.lock();
        condition.signal();
        lock.unlock();
    }
}
