package com.taotao.cloud.sys.biz.api.controller.tools.kafka.dtos;

import lombok.Data;

@Data
public class KafkaConsumerPayload {
    private String clusterName;
    private String topic;
    private String serializable;
    private String classloader;
    private boolean start;
}
