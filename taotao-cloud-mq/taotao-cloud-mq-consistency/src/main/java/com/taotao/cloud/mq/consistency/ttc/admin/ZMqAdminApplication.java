package com.taotao.cloud.mq.consistency.ttc.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Z-MQ 控制台后端入口.
 * <p>
 * 与 {@code z-mq-nameserver} / {@code z-mq-broker} 平级的第三个进程：它自己不碰存储，
 * 只经 {@code z-mq-tools} 的 ClusterAdmin 向 NameServer / Broker 发 RPC，再把结果包成 HTTP REST
 * 给 {@code z-mq-admin} 前端吃。这样控制面与数据面彻底分开，admin 挂了不影响收发消息。
 */
@SpringBootApplication
public class ZMqAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZMqAdminApplication.class, args);
    }
}
