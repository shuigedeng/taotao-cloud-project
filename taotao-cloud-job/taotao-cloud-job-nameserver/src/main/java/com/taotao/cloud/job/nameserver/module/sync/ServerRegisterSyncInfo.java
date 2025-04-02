package com.taotao.cloud.job.nameserver.module.sync;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ServerRegisterSyncInfo extends SyncInfo{
    public ServerRegisterSyncInfo(String scheduleServerIp){
        super(scheduleServerIp);
        this.scheduleServerIp = scheduleServerIp;
    }
    public String scheduleServerIp;
}
