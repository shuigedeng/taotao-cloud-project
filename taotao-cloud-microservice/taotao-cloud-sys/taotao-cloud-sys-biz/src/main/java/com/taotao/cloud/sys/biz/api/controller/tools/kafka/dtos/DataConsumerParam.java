package com.taotao.cloud.sys.biz.api.controller.tools.kafka.dtos;

import lombok.Data;

@Data
public class DataConsumerParam {
    protected String clusterName;
    protected String topic;
    protected int partition;
    protected int perPartitionSize;
    protected String serializer;
    protected String classloaderName;
}
