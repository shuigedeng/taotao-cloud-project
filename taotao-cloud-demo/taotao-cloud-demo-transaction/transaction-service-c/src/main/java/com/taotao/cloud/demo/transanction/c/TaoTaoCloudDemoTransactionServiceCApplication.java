package com.taotao.cloud.demo.transanction.c;

import com.codingapi.txlcn.tc.config.EnableDistributedTransaction;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Description:
 * Date: 2018/12/25
 *
 * @author ujued
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableDistributedTransaction
public class TaoTaoCloudDemoTransactionServiceCApplication {
    public static void main(String[] args) {
        SpringApplication.run(TaoTaoCloudDemoTransactionServiceCApplication.class, args);
    }
}
