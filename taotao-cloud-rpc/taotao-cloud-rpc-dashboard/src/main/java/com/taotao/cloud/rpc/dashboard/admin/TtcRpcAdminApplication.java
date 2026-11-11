package com.taotao.cloud.rpc.dashboard.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ttc-RPC Admin 启动入口（端口 9090）
 */
@SpringBootApplication
public class TtcRpcAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(TtcRpcAdminApplication.class, args);
    }
}
