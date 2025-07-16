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

import com.taotao.cloud.mq.consistency.raft1.common.entity.dto.NodeConfig;
import com.taotao.cloud.mq.consistency.raft1.server.dto.PeerInfoDto;
import com.taotao.cloud.mq.consistency.raft1.server.support.peer.PeerManager;
import java.util.ArrayList;
import java.util.List;

/**
 * @since 1.0.0
 */
public class InnerPeerUtil {

    public static PeerManager initPeerManager(final NodeConfig config) {
        PeerManager peerManager = new PeerManager();

        List<PeerInfoDto> list = new ArrayList<>();
        for (String s : config.getPeerAddressList()) {
            PeerInfoDto peer = new PeerInfoDto(s);
            list.add(peer);

            // 暂时这里简单写死，后续应该调整优化
            if (s.equals("localhost:" + config.getSelfPort())) {
                peerManager.setSelf(peer);
            }
        }

        // 设置全部
        peerManager.setList(list);
        return peerManager;
    }
}
