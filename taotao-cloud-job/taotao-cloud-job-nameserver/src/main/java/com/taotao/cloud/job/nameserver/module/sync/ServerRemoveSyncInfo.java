package com.taotao.cloud.job.nameserver.module.sync;

import lombok.Getter;
import com.taotao.cloud.job.remote.protos.DistroCausa;

@Getter
public class ServerRemoveSyncInfo extends SyncInfo{
    public ServerRemoveSyncInfo(String serverIpAddress){
        super(serverIpAddress);
        this.serverIpAddress = serverIpAddress;
    }
    String serverIpAddress;
}
