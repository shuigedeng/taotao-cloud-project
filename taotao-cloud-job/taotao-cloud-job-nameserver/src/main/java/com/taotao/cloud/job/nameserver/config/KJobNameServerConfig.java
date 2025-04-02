package com.taotao.cloud.job.nameserver.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "kjob.name-server")
public class KJobNameServerConfig {
    private Integer maxWorkerNum = 2;
    private List<String> serverAddressList;

}
