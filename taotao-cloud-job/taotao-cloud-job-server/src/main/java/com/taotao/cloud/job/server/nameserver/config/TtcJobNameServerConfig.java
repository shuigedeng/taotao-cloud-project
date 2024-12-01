package com.taotao.cloud.job.server.nameserver.config;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "ttcjob.name-server")
public class TtcJobNameServerConfig {
    private Integer maxWorkerNum;
}
