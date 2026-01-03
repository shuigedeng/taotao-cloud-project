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

package com.taotao.cloud.rpc.common.idworker;

import com.taotao.cloud.rpc.common.idworker.enums.ServerSelector;
import com.taotao.cloud.rpc.common.idworker.utils.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Sid
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class Sid {

    private static IdWorker idWorker;

    static {
        configure();
    }

    public static synchronized void configure() {
        long workerId = WorkerIdServer.getWorkerId(ServerSelector.REDIS_SERVER.getCode());
        idWorker =
                new IdWorker(workerId) {
                    @Override
                    public long getEpoch() {
                        return TimeUtils.midnightMillis();
                    }
                };
    }

    /**
     * 一天最大毫秒86400000，最大占用27比特 27+10+11=48位 最大值281474976710655(15字)，YK0XXHZ827(10字) 6位(YYMMDD)+15位，共21位
     *
     * @return 固定21位数字字符串
     */
    public static String next() {
        long id = idWorker.nextId();
        String yyMMdd = new SimpleDateFormat("yyMMdd").format(new Date());
        return yyMMdd + String.format("%015d", id);
    }
}
