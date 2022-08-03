package com.taotao.cloud.sys.biz.api.controller.tools.kafka.dtos;

import lombok.Data;

/**
 * 对应 kafka 数据的 Node
 */
@Data
public class BrokerInfo {
    private int id;
    private String host;
    private int port;
    private int jxmPort;

    public BrokerInfo() {
    }

    public BrokerInfo(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }

    public BrokerInfo(int id, String host, int port, int jxmPort) {
        this.id = id;
        this.host = host;
        this.port = port;
        this.jxmPort = jxmPort;
    }

    public String hostAndPort(){
        return host+":"+port;
    }
}
