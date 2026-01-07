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

import com.taotao.cloud.mq.consistency.raft1.server.dto.PeerInfoDto;

/**
 * 分布式节点管理
 *
 * @since 1.1.0
 */
public interface ClusterPeerManager {

    /**
     * 添加
     * @param peerInfoDto 临时对象
     * @return 结果
     */
    ClusterPeerResult addPeer(final PeerInfoDto peerInfoDto);

    /**
     * 移除
     * @param peerInfoDto 临时对象
     * @return 结果
     */
    ClusterPeerResult removePeer(final PeerInfoDto peerInfoDto);
}
