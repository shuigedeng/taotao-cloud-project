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

package com.taotao.cloud.ccsr.common.config;

import com.alipay.sofa.jraft.option.NodeOptions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

/**
 * CcsrConfig
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class CcsrConfig implements Serializable {

    String namespace = "default";

    String rpcType = RpcType.GRPC.getType();

    int port = 8000;

    /**
     * 本地模式
     */
    boolean localMode = false;

    /**
     * 集群地址（集群模式推荐2*N + 1，单机为1）
     */
    List<String> clusterAddress = new ArrayList<>();

    GrpcConfig grpcConfig = new GrpcConfig();

    RaftConfig raftConfig = new RaftConfig();

    NodeOptions nodeOptions = new NodeOptions();

    @Getter
    public enum RpcType {
        HTTP("http"),
        GRPC("grpc"),
        RAFT("raft"),
        WEBSOCKET("websocket");

        private final String type;

        RpcType( String type ) {
            this.type = type;
        }
    }

    @Data
    public static class GrpcConfig {

        private int maxInboundMessageSize = 1024 * 1024 * 10;
        private long keepAliveTime = 10000;
        private long keepAliveTimeout = 10000;
        private long permitKeepAliveTime = 10000;
    }

    @Data
    public static class RaftConfig {

        /**
         * 是否为单节点模式
         */
        boolean isSingleNode = false;

        private int refreshJitterMin = 0;
        private int refreshJitterMax = 5000;

        /**
         * raft 端口偏移
         */
        private int portOffset = 1001;

        /**
         * raft 日志根目录
         */
        private String rootPath = System.getProperty("user.home");

        /**
         * 选举超时时间
         */
        int electionTimeout = 1000;

        /**
         * RPC 请求超时时间，默认 5 秒
         */
        int rpcRequestTimeout = 5000;

        /**
         * 设置快照间隔时间为 1800 秒（即 30 分钟）
         */
        int snapshotIntervalSecs = 1800;

        /**
         * ReadIndex 请求级别，默认 ReadOnlySafe，具体含义参见线性一致读章节
         */
        String readOnlyOption = "read_only_safe";

        /**
         * 节点之间每次文件 RPC (snapshot拷贝）请求的最大大小，默认为 128 K
         */
        private int maxByteCountPerRpc = 128 * 1024;

        /**
         * 是否在拷贝文件中检查文件空洞，暂时未实现
         */
        private boolean fileCheckHole = false;

        /**
         * 从 leader 往 follower 发送的最大日志个数，默认 1024
         */
        private int maxEntriesSize = 1024;

        /**
         * 从 leader 往 follower 发送日志的最大 body 大小，默认 512K
         */
        private int maxBodySize = 512 * 1024;

        /**
         * 日志存储缓冲区最大大小，默认256K
         */
        private int maxAppendBufferSize = 256 * 1024;

        /**
         * 选举定时器间隔会在指定时间之外随机的最大范围，默认1秒
         */
        private int maxElectionDelayMs = 1000;

        /**
         * 指定选举超时时间和心跳间隔时间之间的比值。心跳间隔等于 electionTimeoutMs/electionHeartbeatFactor，默认10分之一。
         */
        private int electionHeartbeatFactor = 10;

        /**
         * 向 leader 提交的任务累积一个批次刷入日志存储的最大批次大小，默认 32 个任务
         */
        private int applyBatch = 32;

        /**
         * 写入日志、元信息的时候必要的时候调用 fsync，通常都应该为 true
         */
        private boolean sync = true;

        /**
         * 写入 snapshot/raft 元信息是否调用 fsync，默认为 false， 在 sync 为 true 的情况下，优选尊重 sync
         */
        private boolean syncMeta = false;

        /**
         * 内部 disruptor buffer 大小，如果是写入吞吐量较高的应用，需要适当调高该值，默认 16384
         */
        private int disruptorBufferSize = 16384;

        /**
         * 是否启用复制的 pipeline 请求优化，默认打开
         */
        private boolean replicatorPipeline = true;

        /**
         * 在启用 pipeline 请求情况下，最大 in-flight 请求数，默认256
         */
        private int maxReplicatorInflightMsgs = 256;

        /**
         * 是否启用 LogEntry checksum
         */
        private boolean enableLogEntryChecksum = false;
    }
}
