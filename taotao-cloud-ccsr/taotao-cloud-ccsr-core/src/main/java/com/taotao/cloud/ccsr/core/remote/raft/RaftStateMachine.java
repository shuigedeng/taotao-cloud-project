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

package com.taotao.cloud.ccsr.core.remote.raft;

import com.alipay.sofa.jraft.Closure;
import com.alipay.sofa.jraft.Iterator;
import com.alipay.sofa.jraft.Node;
import com.alipay.sofa.jraft.Status;
import com.alipay.sofa.jraft.conf.Configuration;
import com.alipay.sofa.jraft.core.StateMachineAdapter;
import com.alipay.sofa.jraft.entity.LeaderChangeContext;
import com.alipay.sofa.jraft.error.RaftError;
import com.alipay.sofa.jraft.error.RaftException;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotReader;
import com.alipay.sofa.jraft.storage.snapshot.SnapshotWriter;
import com.google.protobuf.Message;
import com.taotao.cloud.ccsr.api.grpc.auto.Metadata;
import com.taotao.cloud.ccsr.api.grpc.auto.Response;
import com.taotao.cloud.ccsr.api.result.ResponseHelper;
import com.taotao.cloud.ccsr.common.enums.ResponseCode;
import com.taotao.cloud.ccsr.common.log.Log;
import com.taotao.cloud.ccsr.core.remote.raft.handler.RequestDispatcher;
import com.taotao.cloud.ccsr.core.serializer.Serializer;
import com.taotao.cloud.ccsr.core.storage.MetadaStorage;
import com.taotao.cloud.ccsr.core.utils.ProtoMessageUtils;
import com.taotao.cloud.ccsr.core.utils.StorageHolder;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * @author shuigedeng
 */
public class RaftStateMachine extends StateMachineAdapter {

    protected final RaftServer server;

    protected final RequestDispatcher dispatcher;

    private Node node;

    private final AtomicBoolean isLeader = new AtomicBoolean(false);

    Map<String, Configuration> configurations;

    private final Serializer serializer;

    private String group;

    public RaftStateMachine(
            RaftServer server,
            Serializer serializer,
            Map<String, Configuration> configurations,
            String group) {
        this.group = group;
        this.server = server;
        this.serializer = serializer;
        this.configurations = configurations;
        this.dispatcher = RequestDispatcher.getInstance();
    }

    @Override
    public void onApply(Iterator it) {
        int index = 0;
        int completed = 0;
        RaftClosure closure = null;
        Message message;
        try {
            while (it.hasNext()) {
                Status status = Status.OK();
                try {
                    if (it.done() != null) {
                        closure = (RaftClosure) it.done();
                        message = closure.getMessage();
                    } else {
                        final ByteBuffer data = it.getData();
                        message = ProtoMessageUtils.parse(serializer, data.array());
                    }

                    Log.print("处理Raft Entry：%s", message);

                    if (message != null) {
                        Response response = dispatcher.dispatch(message, message.getClass());
                        if (closure != null) {
                            closure.setResponse(response);
                        }
                    }

                } catch (Throwable t) {
                    index++;
                    status.setError(RaftError.UNKNOWN, t.toString());
                    // 1. 记录错误日志
                    Log.print(
                            "Failed to process raft entry. Error: %s",
                            ExceptionUtils.getRootCauseMessage(t));

                    // 2. 更新闭包状态
                    if (closure != null) {
                        closure.setThrowable(t);
                        closure.setResponse(
                                ResponseHelper.error(
                                        ResponseCode.SYSTEM_ERROR.getCode(),
                                        "Processing failed: " + t.getMessage()));
                    }

                    Optional.ofNullable(closure).ifPresent(c -> c.setThrowable(t));
                    throw t;
                } finally {
                    // FIXME 这里有空指针
                    Optional.ofNullable(closure).ifPresent(c -> c.run(status));
                }

                completed++;
                index++;
                it.next();
            }
        } catch (Throwable e) {
            Log.print("状态机【Critical】异常: %s", ExceptionUtils.getStackTrace(e));
            it.setErrorAndRollback(
                    index - completed,
                    new Status(RaftError.ESTATEMACHINE, "Critical error: %s", e.getMessage()));
        }
    }

    @Override
    public void onLeaderStart(long term) {
        this.isLeader.set(true);
        Log.print("【新Leader】产生：%s，node=%s", term, node.getNodeId());
    }

    @Override
    public void onLeaderStop(Status status) {
        this.isLeader.set(false);
        Log.print("【Leader失效】, status：%s，node=%s", status, node.getNodeId());
    }

    @Override
    public boolean onSnapshotLoad(SnapshotReader reader) {
        try {
            return loadMetadataSnapshot(reader);
        } catch (Exception e) {
            Log.error("快照加载失败", e);
            return false;
        }
    }

    @Override
    public void onSnapshotSave(SnapshotWriter writer, Closure done) {
        try {
            Log.print(
                    "快照保存【开始】Saving snapshot to %s, node: %s", writer.getPath(), node.getNodeId());

            // 将快照信息持久化到磁盘
            saveMetadataSnapshot(writer, done);
            // 实际保存逻辑
            Log.print("快照保存【成功】Saving snapshot to %s, node=%s", writer.getPath(), node.getNodeId());

        } catch (Exception e) {
            Log.error("快照保存失败", e);
            done.run(new Status(RaftError.EIO, "Snapshot save failed"));
        }
    }

    @Override
    public void onConfigurationCommitted(Configuration conf) {
        Log.print("配置提交 conf=%s", conf);
        if (node != null) {
            // 当新配置提交时更新记录
            String groupId = node.getGroupId();
            configurations.put(groupId, conf);
            Log.print("[%s] 配置已更新: %s", groupId, conf);
        }
        super.onConfigurationCommitted(conf);
    }

    @Override
    public void onStopFollowing(LeaderChangeContext ctx) {
        Log.info("onStopFollowing 停止跟随 ctx={}", ctx);
        super.onStopFollowing(ctx);
    }

    @Override
    public void onStartFollowing(LeaderChangeContext ctx) {
        Log.info("onStartFollowing 开始跟随 ctx={}", ctx);
        super.onStartFollowing(ctx);
    }

    @Override
    public void onShutdown() {
        Log.info("关闭状态机...");
        super.onShutdown();
    }

    @Override
    public void onError(RaftException e) {
        Log.error("状态机错误...", e);
        // super.onError(e);
    }

    /**
     * 加载快照, TODO 后续可以考虑从Redis中读取，或者DB中批量加载
     */
    public boolean loadMetadataSnapshot(SnapshotReader reader) {
        Log.print("快照加载 loading snapshot from %s, node=%s", reader.getPath(), node);

        String snapshotFilePath = reader.getPath() + File.separator + "metadata_config.data";
        File snapshotFile = new File(snapshotFilePath);

        if (!snapshotFile.exists()) {
            Log.error("快照文件不存在: %s", snapshotFilePath);
            return false;
        }

        try (FileInputStream fis = new FileInputStream(snapshotFile)) {
            // 1. 读取快照数据
            byte[] data = new byte[(int) snapshotFile.length()];
            fis.read(data);

            // 2. 反序列化并恢复存储
            MetadaStorage storage = StorageHolder.getInstance("metadata");
            Map<String, Metadata> snapshot = serializer.deserialize(data);
            storage.getStorage().clear();
            storage.getStorage().putAll(serializer.deserialize(data));

            Log.print("快照加载成功: %s", snapshotFilePath);
            Log.print("快照加载内容为: %s", snapshot);
            return true;
        } catch (Exception e) {
            Log.error("快照加载失败", e);
            return false;
        }
    }

    //    public static void main(String[] args) {
    //        String snapshotFilePath =
    // "/Users/caoshipeng/IdeaProjects/ccsr/raft/config_center_group/127.0.0.1_9001/snapshot/snapshot_13/metadata_config.data";
    //        File snapshotFile = new File(snapshotFilePath);
    //        try (FileInputStream fis = new FileInputStream(snapshotFile)) {
    //            // 1. 读取快照数据
    //            byte[] data = new byte[(int) snapshotFile.length()];
    //            fis.read(data);
    //
    //            // 2. 反序列化并恢复存储
    //            MetadaStorage storage = StorageHolder.getInstance("metadata");
    //            Map<String, Metadata> snapshot = SerializeFactory.getDefault().deserialize(data);
    //
    //
    //        } catch (IOException e) {
    //            throw new RuntimeException(e);
    //        }
    //    }

    /**
     * 保存快照, TODO 后续可以考虑持久化到磁盘后，同时向DB/Redis持久化
     */
    private void saveMetadataSnapshot(SnapshotWriter writer, Closure done) {
        String snapshotFilePath = writer.getPath() + File.separator + "metadata_config.data";
        File snapshotFile = new File(snapshotFilePath);

        try (FileOutputStream fos = new FileOutputStream(snapshotFile)) {
            // 1. 序列化存储数据
            MetadaStorage storage = StorageHolder.getInstance("metadata");
            Map<String, Metadata> snapshot = storage.getStorage();
            byte[] data = serializer.serialize(snapshot);

            // 2. 写入快照文件
            fos.write(data);

            // 3. 将文件添加到快照元数据
            if (writer.addFile(snapshotFile.getName())) {
                done.run(Status.OK());
                Log.print("快照保存成功: %s", snapshotFilePath);
            } else {
                done.run(new Status(RaftError.EIO, "Failed to add snapshot file"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void triggerSnapshot() {
        node.snapshot(
                new Closure() {
                    @Override
                    public void run(Status status) {
                        if (status.isOk()) {
                            Log.print("手动快照成功");
                        } else {
                            Log.error("手动快照失败: %s", status.getErrorMsg());
                        }
                    }
                });
    }

    public boolean isLeader() {
        return isLeader.get();
    }

    public void setNode(Node node) {
        this.node = node;
    }
}
