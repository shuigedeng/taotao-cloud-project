package com.taotao.cloud.job.nameserver.module.sync;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class WorkerSubscribeSyncInfo extends SyncInfo{
    public WorkerSubscribeSyncInfo(String workerIpAddress, String appName, long scheduleTime, String serverIpAddress){
        super(workerIpAddress);
        this.workerIpAddress = workerIpAddress;
        this.appName = appName;
        this.scheduleTime = scheduleTime;
        this.serverIpAddress = serverIpAddress;
    }
    String workerIpAddress;
    String appName;
    long scheduleTime;
    String serverIpAddress;
}
