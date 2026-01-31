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

import java.util.ArrayList;
import java.util.List;

/**
 * PeerManager
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
public class PeerManager {

    /**
     * 全部的节点
     */
    private List<PeerInfoDto> list = new ArrayList<>();

    /**
     * 管理这
     */
    private volatile PeerInfoDto leader;

    /**
     * 自己
     */
    private volatile PeerInfoDto self;

    public List<PeerInfoDto> getList() {
        return list;
    }

    public void setList( List<PeerInfoDto> list ) {
        this.list = list;
    }

    public PeerInfoDto getLeader() {
        return leader;
    }

    public void setLeader( PeerInfoDto leader ) {
        this.leader = leader;
    }

    public PeerInfoDto getSelf() {
        return self;
    }

    public void setSelf( PeerInfoDto self ) {
        this.self = self;
    }

    public List<PeerInfoDto> getPeersWithOutSelf() {
        List<PeerInfoDto> result = new ArrayList<>(list);
        result.remove(self);
        return result;
    }
}
