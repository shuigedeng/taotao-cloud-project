package com.taotao.cloud.job.server.jobserver.remote.worker;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.taotao.cloud.job.common.domain.WorkerHeartbeat;
import com.taotao.cloud.job.server.jobserver.common.module.WorkerInfo;
import lombok.extern.slf4j.Slf4j;


import java.util.List;
import java.util.Map;

/**
 * 管理Worker集群状态
 *
 * @author shuigedeng
 * @since 2020/4/5
 */
@Slf4j
public class ClusterStatusHolder {

    /**
     * 集群所属的应用名称
     */
    private final String appName;
    /**
     * 集群中所有机器的信息
     */
    private final Map<String, WorkerInfo> address2WorkerInfo;



    public ClusterStatusHolder(String appName) {
        this.appName = appName;
        address2WorkerInfo = Maps.newConcurrentMap();
    }

    /**
     * 更新 worker 机器的状态
     * @param heartbeat 心跳请求
     */
    public void updateStatus(WorkerHeartbeat heartbeat) {

        String workerAddress = heartbeat.getWorkerAddress();
        long heartbeatTime = heartbeat.getHeartbeatTime();

        WorkerInfo workerInfo = address2WorkerInfo.computeIfAbsent(workerAddress, ignore -> {
            WorkerInfo wf = new WorkerInfo();
            wf.refresh(heartbeat);
            return wf;
        });
        long oldTime = workerInfo.getLastActiveTime();
        if (heartbeatTime < oldTime) {
            log.warn("[ClusterStatusHolder-{}] receive the expired heartbeat from {}, serverTime: {}, heartTime: {}", appName, heartbeat.getWorkerAddress(), System.currentTimeMillis(), heartbeat.getHeartbeatTime());
            return;
        }

        workerInfo.refresh(heartbeat);


    }

    /**
     * 获取该集群所有的机器信息
     * @return 地址: 机器信息
     */
    public Map<String, WorkerInfo> getAllWorkers() {
        return address2WorkerInfo;
    }



    /**
     * 释放所有本地存储的容器信息（该操作会导致短暂的 listDeployedContainer 服务不可用）
     */
    public void release() {
        log.info("[ClusterStatusHolder-{}] clean the containerInfos, listDeployedContainer service may down about 1min~", appName);


        // 丢弃超时机器的信息
        List<String> timeoutAddress = Lists.newLinkedList();
        address2WorkerInfo.forEach((addr, workerInfo) -> {
            if (workerInfo.timeout()) {
                timeoutAddress.add(addr);
            }
        });

        if (!timeoutAddress.isEmpty()) {
            log.info("[ClusterStatusHolder-{}] detective timeout workers({}), try to release their infos.", appName, timeoutAddress);
            timeoutAddress.forEach(address2WorkerInfo::remove);
        }
    }
}
