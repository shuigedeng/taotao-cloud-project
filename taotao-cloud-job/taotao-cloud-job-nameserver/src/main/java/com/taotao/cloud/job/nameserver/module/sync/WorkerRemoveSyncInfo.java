package com.taotao.cloud.job.nameserver.module.sync;

import lombok.Getter;
import com.taotao.cloud.job.remote.protos.DistroCausa;

@Getter
public class WorkerRemoveSyncInfo extends SyncInfo{
    public WorkerRemoveSyncInfo(String clientIp, String appName){
        super(clientIp);
        this.appName = appName;
    }
    String appName;
}
