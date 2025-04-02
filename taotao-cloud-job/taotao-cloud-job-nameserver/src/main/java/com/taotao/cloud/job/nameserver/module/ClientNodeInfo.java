package com.taotao.cloud.job.nameserver.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.taotao.cloud.job.common.domain.WorkerHeartbeat;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClientNodeInfo {
    private String type;
    private String appName;
    private long lastActiveTime;
    private static final long CLIENT_TIMEOUT_MS = 60000;

    public void refresh(ClientHeartbeat clientHeartbeat) {
        lastActiveTime = clientHeartbeat.getHeartbeatTime();
        appName = clientHeartbeat.getAppName();
    }

    public boolean timeout() {
        long timeout = System.currentTimeMillis() - lastActiveTime;
        return timeout > CLIENT_TIMEOUT_MS;
    }

}
