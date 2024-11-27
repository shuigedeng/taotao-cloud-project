package com.taotao.cloud.job.server.nameserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
public class TtcJobNameServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtcJobNameServerApplication.class, args);
    }

}
