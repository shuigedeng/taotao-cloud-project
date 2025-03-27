package com.taotao.cloud.message.biz.austin.support.mq.springeventbus;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;

import java.io.Serializable;

/**
 * @author shuigedeng
 */
@Data
@Accessors(chain=true)
public class AustinSpringEventSource implements Serializable {
    private String topic;
    private String jsonValue;
    private String tagId;
}
