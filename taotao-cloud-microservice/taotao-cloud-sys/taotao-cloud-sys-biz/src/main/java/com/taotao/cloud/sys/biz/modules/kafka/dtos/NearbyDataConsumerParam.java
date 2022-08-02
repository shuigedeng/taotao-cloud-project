package com.taotao.cloud.sys.biz.modules.kafka.dtos;

import lombok.Data;

@Data
public class NearbyDataConsumerParam extends DataConsumerParam{
    private long offset;
}
