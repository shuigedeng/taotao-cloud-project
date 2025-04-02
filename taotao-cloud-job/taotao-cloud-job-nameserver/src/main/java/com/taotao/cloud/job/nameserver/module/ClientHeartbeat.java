package com.taotao.cloud.job.nameserver.module;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ClientHeartbeat {
    String ip;
    String clientType;
    Long heartbeatTime;
    String appName;
}
