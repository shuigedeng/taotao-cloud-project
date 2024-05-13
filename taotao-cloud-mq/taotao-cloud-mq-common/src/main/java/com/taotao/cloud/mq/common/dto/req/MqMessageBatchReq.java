package com.taotao.cloud.mq.common.dto.req;

import java.util.List;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqMessageBatchReq extends MqCommonReq {

    private List<MqMessage> mqMessageList;

    public List<MqMessage> getMqMessageList() {
        return mqMessageList;
    }

    public void setMqMessageList(List<MqMessage> mqMessageList) {
        this.mqMessageList = mqMessageList;
    }
}
