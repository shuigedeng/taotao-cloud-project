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

package com.taotao.cloud.sys.biz.supports.largefile.util;

import java.util.concurrent.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SimpleAsyncExec
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class SimpleAsyncExec {

    private static final SimpleAsyncExec inst = new SimpleAsyncExec();
    private final ExecutorService executor = Executors.newFixedThreadPool(5);
    private static final Logger logger = LoggerFactory.getLogger(SimpleAsyncExec.class);

    public static SimpleAsyncExec getInstance() {
        return inst;
    }

    public <T> T exec( Callable callable ) {
        T t = null;
        // logger.info("SimpleAsyncExec exec start。。。");
        Future<T> future = executor.submit(callable);
        try {
            t = future.get();
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (ExecutionException e) {
            logger.error(e.getMessage(), e);
        }

        return t;
    }

    public void exec( Runnable run ) {
        // logger.info("SimpleAsyncExec exec start。。。");
        executor.submit(run);
    }
}
