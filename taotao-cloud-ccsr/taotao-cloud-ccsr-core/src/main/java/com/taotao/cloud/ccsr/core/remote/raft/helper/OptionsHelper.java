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

package com.taotao.cloud.ccsr.core.remote.raft.helper;

import com.alipay.sofa.jraft.option.*;
import com.taotao.cloud.ccsr.common.config.CcsrConfig;
import com.taotao.boot.common.utils.lang.StringUtils;

/**
 * @author shuigedeng
 */
public class OptionsHelper {

    private static final CliOptions cliOptions = new CliOptions();
    private static final RaftOptions raftOptions = new RaftOptions();
    private static final NodeOptions nodeOptions = new NodeOptions();

    public static NodeOptions initNodeOptions(CcsrConfig.RaftConfig config) {
        // 启用共享选举定时器
        nodeOptions.setSharedElectionTimer(true);
        // 用共享投票定时器
        nodeOptions.setSharedVoteTimer(true);
        // 启用共享下台定时器
        nodeOptions.setSharedStepDownTimer(true);
        // 启用共享快照定时器
        nodeOptions.setSharedSnapshotTimer(true);
        // 启用指标收集功能
        // nodeOptions.setEnableMetrics(true);
        // 选举超时时间
        nodeOptions.setElectionTimeoutMs(config.getElectionTimeout());
        // 设置快照间隔时间为 1800 秒（即 30 分钟）
        // nodeOptions.setSnapshotIntervalSecs(config.getSnapshotIntervalSecs());
        // 当日志条数超过 1000 条时触发快照，二者为同时满足的关系
        // nodeOptions.setSnapshotLogIndexMargin(1000);
        // TODO 本地测试，60s生成快照
        nodeOptions.setSnapshotIntervalSecs(60);

        nodeOptions.setRaftOptions(initRaftOptions(config));
        nodeOptions.setRpcConnectTimeoutMs(3000);
        nodeOptions.setRpcDefaultTimeout(3000);

        // 如果是单节点启动单话：
        if (config.isSingleNode()) {
            // 单节点必须配置的选项
            nodeOptions.setElectionPriority(100); // 确保节点成为Leader
            // nodeOptions.setElectionTimeoutMs(Integer.MAX_VALUE); // 可以设置超长选举超时以避免触发
        }

        return nodeOptions;
    }

    public static RaftOptions initRaftOptions(CcsrConfig.RaftConfig config) {
        raftOptions.setReadOnlyOptions(mathReadOnlyOption(config));
        raftOptions.setMaxByteCountPerRpc(config.getMaxByteCountPerRpc());
        raftOptions.setMaxEntriesSize(config.getMaxEntriesSize());
        raftOptions.setMaxBodySize(config.getMaxBodySize());
        raftOptions.setMaxAppendBufferSize(config.getMaxAppendBufferSize());
        raftOptions.setMaxElectionDelayMs(config.getMaxElectionDelayMs());
        raftOptions.setElectionHeartbeatFactor(config.getElectionHeartbeatFactor());
        raftOptions.setApplyBatch(config.getApplyBatch());
        raftOptions.setSync(config.isSync());
        raftOptions.setSyncMeta(config.isSyncMeta());
        raftOptions.setDisruptorBufferSize(config.getDisruptorBufferSize());
        raftOptions.setReplicatorPipeline(config.isReplicatorPipeline());
        raftOptions.setReplicatorPipeline(config.isReplicatorPipeline());
        raftOptions.setMaxReplicatorInflightMsgs(config.getMaxReplicatorInflightMsgs()); // 减少内存占用

        raftOptions.setEnableLogEntryChecksum(config.isEnableLogEntryChecksum());
        return raftOptions;
    }

    public static CliOptions initCliOptions(CcsrConfig.RaftConfig config) {
        cliOptions.setTimeoutMs(5000);
        cliOptions.setMaxRetry(3);
        return cliOptions;
    }

    private static ReadOnlyOption mathReadOnlyOption(CcsrConfig.RaftConfig config) {
        String safe = "read_only_safe";
        String leaseBased = "read_only_lease_based";

        if (StringUtils.isBlank(config.getReadOnlyOption())
                || StringUtils.equals(safe, config.getReadOnlyOption())) {
            return ReadOnlyOption.ReadOnlySafe;
        }

        if (StringUtils.equals(leaseBased, config.getReadOnlyOption())) {
            return ReadOnlyOption.ReadOnlyLeaseBased;
        }
        throw new IllegalArgumentException(
                "Illegal Raft system parameters => ReadOnlyOption"
                        + " : ["
                        + config.getReadOnlyOption()
                        + "], should be 'read_only_safe' or 'read_only_lease_based'");
    }

    public static CliOptions getCliOptions() {
        return cliOptions;
    }

    public static RaftOptions getRaftOptions() {
        return raftOptions;
    }

    public static NodeOptions getNodeOptions() {
        return nodeOptions;
    }
}
