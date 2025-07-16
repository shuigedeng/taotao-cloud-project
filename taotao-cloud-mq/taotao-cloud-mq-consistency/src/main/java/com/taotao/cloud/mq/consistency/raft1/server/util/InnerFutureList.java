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

package com.taotao.cloud.mq.consistency.raft1.server.util;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

import com.taotao.cloud.mq.consistency.raft1.server.support.concurrent.RaftThreadPool;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InnerFutureList {

    private static final Logger log = LoggerFactory.getLogger(InnerFutureList.class);

    public static List<Boolean> getRPCAppendResult(
            List<Future<Boolean>> futureList, CountDownLatch latch) {
        final List<Boolean> resultList = new CopyOnWriteArrayList<>();
        for (Future<Boolean> future : futureList) {
            RaftThreadPool.execute(
                    () -> {
                        try {
                            resultList.add(future.get(3000, MILLISECONDS));
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                            resultList.add(false);
                        } finally {
                            latch.countDown();
                        }
                    });
        }

        return resultList;
    }
}
