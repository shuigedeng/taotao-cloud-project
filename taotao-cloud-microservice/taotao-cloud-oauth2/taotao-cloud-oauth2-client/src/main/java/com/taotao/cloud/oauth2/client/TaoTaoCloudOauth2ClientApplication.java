package com.taotao.cloud.oauth2.client;

import com.taotao.cloud.loadbalancer.annotation.EnableTaoTaoCloudFeign;
import com.taotao.cloud.loadbalancer.annotation.EnableTaoTaoCloudHttpClient;
import com.taotao.cloud.loadbalancer.annotation.EnableTaoTaoCloudLoadbalancer;
import com.taotao.cloud.p6spy.annotation.EnableTaoTaoCloudP6spy;
import com.taotao.cloud.sentinel.annotation.EnableTaoTaoCloudSentinel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableTaoTaoCloudLoadbalancer
@EnableTaoTaoCloudHttpClient
@EnableTaoTaoCloudFeign
@EnableTaoTaoCloudP6spy
@EnableTaoTaoCloudSentinel
@SpringBootApplication
@EnableDiscoveryClient
public class TaoTaoCloudOauth2ClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaoTaoCloudOauth2ClientApplication.class, args);
    }
}
