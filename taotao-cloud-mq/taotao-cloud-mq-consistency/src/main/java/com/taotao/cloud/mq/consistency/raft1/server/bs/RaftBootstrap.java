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

package com.taotao.cloud.mq.consistency.raft1.server.bs;

import com.alibaba.fastjson2.JSON;
import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.mq.consistency.raft1.common.entity.dto.NodeConfig;
import com.taotao.cloud.mq.consistency.raft1.server.core.Node;
import com.taotao.cloud.mq.consistency.raft1.server.core.impl.DefaultNode;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * raft 启动引导类
 *
 * @since 1.0.0
 */
public class RaftBootstrap {

    private static final Logger log = LoggerFactory.getLogger(RaftBootstrap.class);

    /**
     * 当前服务节点
     */
    private final int serverPort;

    /**
     * 集群启动列表
     */
    private final List<String> clusterAddressList;

    /**
     * 默认节点
     */
    private Node node = new DefaultNode();

    public RaftBootstrap(int serverPort, List<String> clusterAddressList) {
        ArgUtils.notEmpty(clusterAddressList, "clusterAddressList");
        ArgUtils.notNegative(serverPort, "serverPort");

        this.serverPort = serverPort;
        this.clusterAddressList = clusterAddressList;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public void boot() throws Throwable {
        // 配置信息
        NodeConfig config = new NodeConfig();
        // 自身节点
        config.setSelfPort(serverPort);
        // 其他节点地址
        config.setPeerAddressList(clusterAddressList);
        node.setConfig(config);
        log.info("[Rate] config={}", JSON.toJSONString(config));
        node.init();

        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    synchronized (node) {
                                        node.notifyAll();
                                    }
                                }));

        log.info("[Raft] RaftBootstrap gracefully wait");

        synchronized (node) {
            node.wait();
        }

        log.info("[Raft] RaftBootstrap gracefully stop");
        node.destroy();
    }
}
