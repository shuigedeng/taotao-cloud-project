package com.taotao.cloud.job.nameserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "ttcjob.name-server")
public class TtcJobNameServerConfig {
    private Integer maxWorkerNum = 2;
    private List<String> serverAddressList;

}
