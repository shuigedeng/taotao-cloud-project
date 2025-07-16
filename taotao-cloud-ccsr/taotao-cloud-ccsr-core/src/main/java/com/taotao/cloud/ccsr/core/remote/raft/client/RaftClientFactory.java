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

package com.taotao.cloud.ccsr.core.remote.raft.client;

import com.alipay.sofa.jraft.entity.PeerId;
import com.alipay.sofa.jraft.rpc.RpcClient;
import com.google.common.eventbus.Subscribe;
import com.taotao.cloud.ccsr.api.event.GlobalEventBus;
import com.taotao.cloud.ccsr.api.listener.Listener;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.core.event.LeaderRefreshEvent;
import com.taotao.cloud.ccsr.core.remote.raft.helper.RaftHelper;

/**
 * @author shuigedeng
 */
public class RaftClientFactory {

    // Raft RPC
    private RpcClient rpcClient;

    private PeerId leaderId;

    public boolean initialize = false;

    private static RaftClientFactory INSTANCE;

    {
        GlobalEventBus.register(new LeaderRefreshListener());
    }

    public class LeaderRefreshListener implements Listener<LeaderRefreshEvent> {

        @Override
        @Subscribe
        public void onSubscribe(LeaderRefreshEvent event) {
            Log.print("LeaderRefreshListener->收到监听消息, leaderId=%s", event.peerId());
            leaderId = event.peerId();
        }
    }

    private RaftClientFactory() {}

    public static RaftClientFactory getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RaftClientFactory();
        }
        return INSTANCE;
    }

    // @Bean(initMethod="init")
    public void init() {
        this.rpcClient = RaftHelper.initClient();
        this.rpcClient.init(null);
        initialize = true;
    }

    public PeerId getLeaderId() {
        return leaderId;
    }

    public RpcClient getRpcClient() {
        return rpcClient;
    }
}
