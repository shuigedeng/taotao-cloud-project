package com.taotao.cloud.sys.biz.modules.kafka.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendJsonDataParam {
    protected String clusterName;
    protected String topic;
    protected String key;
    protected String data;

    public SendJsonDataParam() {
    }

    /**
     * @param key
     * @param data
     */
    public SendJsonDataParam(String clusterName, String topic, String key, String data) {
        this.clusterName = clusterName;
        this.topic = topic;
        this.key = key;
        this.data = data;
    }

}
