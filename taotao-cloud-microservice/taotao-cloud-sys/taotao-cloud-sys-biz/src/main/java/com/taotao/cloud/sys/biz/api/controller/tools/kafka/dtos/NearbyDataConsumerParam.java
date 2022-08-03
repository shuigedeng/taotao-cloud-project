package com.taotao.cloud.sys.biz.api.controller.tools.kafka.dtos;

import lombok.Data;

@Data
public class NearbyDataConsumerParam extends DataConsumerParam{
    private long offset;
}
