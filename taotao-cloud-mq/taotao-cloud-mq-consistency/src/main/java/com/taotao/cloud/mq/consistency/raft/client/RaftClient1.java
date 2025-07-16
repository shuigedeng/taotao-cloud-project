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

package com.taotao.cloud.mq.consistency.raft.client;

import com.taotao.cloud.mq.consistency.raft.current.SleepHelper;
import com.taotao.cloud.mq.consistency.raft.entity.LogEntry;
import lombok.extern.slf4j.Slf4j;

/**
 * @author shuigedeng
 */
@Slf4j
public class RaftClient1 {

    public static void main(String[] args) throws Throwable {

        RaftClientRPC rpc = new RaftClientRPC();

        for (int i = 3; i > -1; i++) {
            try {
                String key = "hello:" + i;
                String value = "world:" + i;

                String putResult = rpc.put(key, value);

                log.info("key = {}, value = {}, put response : {}", key, value, putResult);

                SleepHelper.sleep(1000);

                LogEntry logEntry = rpc.get(key);

                log.info("key = {}, value = {}, get response : {}", key, value, logEntry);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                i = i - 1;
            }

            SleepHelper.sleep(5000);
        }
    }
}
