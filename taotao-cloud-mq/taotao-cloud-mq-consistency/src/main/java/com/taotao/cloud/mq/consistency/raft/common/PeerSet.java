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

package com.taotao.cloud.mq.consistency.raft.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * 节点集合. 去重.
 *
 * @author shuigedeng
 */
public class PeerSet implements Serializable {

    private List<Peer> list = new ArrayList<>();

    private volatile Peer leader;

    /**
     * final
     */
    private volatile Peer self;

    private PeerSet() {
    }

    public static PeerSet getInstance() {
        return PeerSetLazyHolder.INSTANCE;
    }

    /**
     * PeerSetLazyHolder
     *
     * @author shuigedeng
     * @version 2026.03
     * @since 2025-12-19 09:30:45
     */
    private static class PeerSetLazyHolder {

        private static final PeerSet INSTANCE = new PeerSet();
    }

    public void setSelf( Peer peer ) {
        self = peer;
    }

    public Peer getSelf() {
        return self;
    }

    public void addPeer( Peer peer ) {
        list.add(peer);
    }

    public void removePeer( Peer peer ) {
        list.remove(peer);
    }

    public List<Peer> getPeers() {
        return list;
    }

    public List<Peer> getPeersWithOutSelf() {
        List<Peer> list2 = new ArrayList<>(list);
        list2.remove(self);
        return list2;
    }

    public Peer getLeader() {
        return leader;
    }

    public void setLeader( Peer peer ) {
        leader = peer;
    }

    @Override
    public String toString() {
        return "PeerSet{" + "list=" + list + ", leader=" + leader + ", self=" + self + '}';
    }
}
