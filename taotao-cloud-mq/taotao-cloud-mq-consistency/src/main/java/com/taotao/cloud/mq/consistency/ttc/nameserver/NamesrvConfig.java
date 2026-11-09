package com.taotao.cloud.mq.consistency.ttc.nameserver;

import java.io.File;

/**
 * NameServer 配置（对标 RocketMQ NamesrvConfig）.
 * <p>
 * 字段命名与 RocketMQ 保持一致, KV 配置缺省存放在
 * {@code ${user.home}/zmq/namesrv/kvConfig.json}.
 * <p>
 * 该类被 main 入口以及单元测试共享, 故设为 public.
 */
public class NamesrvConfig {

    private String kvConfigPath = System.getProperty("user.home") + File.separator
            + "zmq" + File.separator + "namesrv" + File.separator + "kvConfig.json";

    public String getKvConfigPath() {
        return kvConfigPath;
    }

    public void setKvConfigPath(String kvConfigPath) {
        this.kvConfigPath = kvConfigPath;
    }
}
