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

package com.taotao.cloud.mq.consistency.raft1.server.support.peer;

import com.taotao.boot.common.utils.common.ArgUtils;
import com.taotao.cloud.mq.consistency.raft1.common.constant.RpcRequestCmdConst;
import com.taotao.cloud.mq.consistency.raft1.common.constant.enums.NodeStatusEnum;
import com.taotao.cloud.mq.consistency.raft1.common.entity.req.dto.LogEntry;
import com.taotao.cloud.mq.consistency.raft1.common.rpc.RpcRequest;
import com.taotao.cloud.mq.consistency.raft1.server.dto.NodeInfoContext;
import com.taotao.cloud.mq.consistency.raft1.server.dto.PeerInfoDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 默认实现
 *
 * @since 1.1.0
 */
public class DefaultClusterPeerManager implements ClusterPeerManager {

    private static final Logger log = LoggerFactory.getLogger(DefaultClusterPeerManager.class);

    private final NodeInfoContext node;

    public DefaultClusterPeerManager(NodeInfoContext node) {
        ArgUtils.notNull(node, "node");

        this.node = node;
    }

    @Override
    public synchronized ClusterPeerResult addPeer(PeerInfoDto newPeer) {
        // 已经存在
        if (node.getPeerManager().getPeersWithOutSelf().contains(newPeer)) {
            log.warn("add peer exists={}", newPeer);

            return new ClusterPeerResult();
        }

        // 如果当前不是 leader 怎么办？
        node.getPeerManager().getPeersWithOutSelf().add(newPeer);

        if (node.getStatus() == NodeStatusEnum.LEADER) {
            node.getNextIndexes().put(newPeer, 0L);
            node.getMatchIndexes().put(newPeer, 0L);

            for (long i = 0; i < node.getLogManager().getLastIndex(); i++) {
                LogEntry entry = node.getLogManager().read(i);
                if (entry != null) {
                    // 备份
                    node.getRaftReplication().replication(node, newPeer, entry);
                }
            }

            for (PeerInfoDto ignore : node.getPeerManager().getPeersWithOutSelf()) {
                // TODO 同步到其他节点.
                RpcRequest request = new RpcRequest();
                request.setCmd(RpcRequestCmdConst.CHANGE_CONFIG_ADD);
                request.setUrl(newPeer.getAddress());
                request.setObj(newPeer);

                ClusterPeerResult result = node.getRpcClient().send(request);
                if (result != null && result.isSuccess()) {
                    log.info(
                            "replication config success, peer : {}, newServer : {}",
                            newPeer,
                            newPeer);
                } else {
                    // 失败了会怎么样？
                    log.warn(
                            "replication config fail, peer : {}, newServer : {}", newPeer, newPeer);
                }
            }
        }

        return new ClusterPeerResult();
    }

    @Override
    public synchronized ClusterPeerResult removePeer(PeerInfoDto oldPeer) {
        node.getPeerManager().getPeersWithOutSelf().remove(oldPeer);
        node.getNextIndexes().remove(oldPeer);
        node.getMatchIndexes().remove(oldPeer);

        return new ClusterPeerResult();
    }
}
