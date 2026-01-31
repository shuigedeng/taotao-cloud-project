/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.mq.consistency.raft1.server.support.concurrent;

import java.util.concurrent.*;

/**
 * @since 1.0.0
 */
public class RaftThreadPool {

    private static final int CUP = Runtime.getRuntime().availableProcessors();
    private static final int MAX_POOL_SIZE = CUP * 2;
    private static final int QUEUE_SIZE = 1024;
    private static final long KEEP_TIME = 1000 * 60;
    private static final TimeUnit KEEP_TIME_UNIT = TimeUnit.MILLISECONDS;

    private static ScheduledExecutorService ss = getScheduled();
    private static ThreadPoolExecutor te = getThreadPool();

    private static ThreadPoolExecutor getThreadPool() {
        return new RaftThreadPoolExecutor(
                CUP,
                MAX_POOL_SIZE,
                KEEP_TIME,
                KEEP_TIME_UNIT,
                new LinkedBlockingQueue<>(QUEUE_SIZE),
                new NameThreadFactory());
    }

    private static ScheduledExecutorService getScheduled() {
        return new ScheduledThreadPoolExecutor(CUP, new NameThreadFactory());
    }

    public static void scheduleAtFixedRate( Runnable r, long initDelay, long delayMills ) {
        ss.scheduleAtFixedRate(r, initDelay, delayMills, TimeUnit.MILLISECONDS);
    }

    public static void scheduleWithFixedDelay( Runnable r, long delayMills ) {
        ss.scheduleWithFixedDelay(r, 0, delayMills, TimeUnit.MILLISECONDS);
    }

    @SuppressWarnings("unchecked")
    public static <T> Future<T> submit( Callable r ) {
        return te.submit(r);
    }

    public static void execute( Runnable r ) {
        te.execute(r);
    }

    public static void execute( Runnable r, boolean sync ) {
        if (sync) {
            r.run();
        } else {
            te.execute(r);
        }
    }

    /**
     * NameThreadFactory
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    static class NameThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread( Runnable r ) {
            Thread t = new RaftThread("Raft thread", r);
            t.setDaemon(true);
            t.setPriority(5);
            return t;
        }
    }
}
