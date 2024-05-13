package com.taotao.cloud.mq.common.dto.resp;

import java.io.Serializable;

/**
 * 消息消费结果
 * @author shuigedeng
 * @since 2024.05
 */
public class MqConsumerResultResp extends MqCommonResp {

    /**
     * 消费状态
     * @since 2024.05
     */
    private String consumerStatus;

    public String getConsumerStatus() {
        return consumerStatus;
    }

    public void setConsumerStatus(String consumerStatus) {
        this.consumerStatus = consumerStatus;
    }
}
