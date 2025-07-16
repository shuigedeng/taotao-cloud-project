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

import com.google.common.collect.Lists;
import com.taotao.cloud.mq.consistency.raft.entity.LogEntry;
import com.taotao.cloud.mq.consistency.raft.rpc.DefaultRpcClient;
import com.taotao.cloud.mq.consistency.raft.rpc.Request;
import com.taotao.cloud.mq.consistency.raft.rpc.RpcClient;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author gwk_2
 * @since 2022/4/19 15:50
 */
public class RaftClientRPC {

    private static List<String> list =
            Lists.newArrayList("127.0.0.1:8777", "127.0.0.1:8778", "127.0.0.1:8779");

    private static final RpcClient CLIENT = new DefaultRpcClient();

    private AtomicLong count = new AtomicLong(3);

    public RaftClientRPC() throws Throwable {
        CLIENT.init();
    }

    /**
     * @param key
     * @return
     */
    public LogEntry get(String key) {
        ClientKVReq obj = ClientKVReq.builder().key(key).type(ClientKVReq.GET).build();

        int index = (int) (count.incrementAndGet() % list.size());

        String addr = list.get(index);

        ClientKVAck response;
        Request r = Request.builder().obj(obj).url(addr).cmd(Request.CLIENT_REQ).build();
        try {
            response = CLIENT.send(r);
        } catch (Exception e) {
            r.setUrl(list.get((int) ((count.incrementAndGet()) % list.size())));
            response = CLIENT.send(r);
        }

        return (LogEntry) response.getResult();
    }

    /**
     * @param key
     * @param value
     * @return
     */
    public String put(String key, String value) {
        int index = (int) (count.incrementAndGet() % list.size());

        String addr = list.get(index);
        ClientKVReq obj = ClientKVReq.builder().key(key).value(value).type(ClientKVReq.PUT).build();

        Request r = Request.builder().obj(obj).url(addr).cmd(Request.CLIENT_REQ).build();
        ClientKVAck response;
        try {
            response = CLIENT.send(r);
        } catch (Exception e) {
            r.setUrl(list.get((int) ((count.incrementAndGet()) % list.size())));
            response = CLIENT.send(r);
        }

        return response.getResult().toString();
    }
}
