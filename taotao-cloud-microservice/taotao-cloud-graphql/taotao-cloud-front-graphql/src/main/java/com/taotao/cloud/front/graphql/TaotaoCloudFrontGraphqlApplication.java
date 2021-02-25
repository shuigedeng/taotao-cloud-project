package com.taotao.cloud.front.graphql;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class TaotaoCloudFrontGraphqlApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaotaoCloudFrontGraphqlApplication.class, args);
    }

}
