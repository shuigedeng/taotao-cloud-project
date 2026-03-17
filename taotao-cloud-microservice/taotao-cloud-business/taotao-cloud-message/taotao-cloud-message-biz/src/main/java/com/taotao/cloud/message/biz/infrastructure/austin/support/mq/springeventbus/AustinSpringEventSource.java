package com.taotao.cloud.message.biz.infrastructure.austin.support.mq.springeventbus;

import lombok.Data;

import java.io.Serializable;

/**
 * @author shuigedeng
 */
@Data

public class AustinSpringEventSource implements Serializable {
    private String topic;
    private String jsonValue;
    private String tagId;
}
