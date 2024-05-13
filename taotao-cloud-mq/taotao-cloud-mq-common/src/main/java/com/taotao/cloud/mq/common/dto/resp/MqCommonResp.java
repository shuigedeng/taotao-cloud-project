package com.taotao.cloud.mq.common.dto.resp;

import java.io.Serializable;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public class MqCommonResp implements Serializable {

    /**
     * 响应编码
     * @since 2024.05
     */
    private String respCode;

    /**
     * 响应消息
     * @since 2024.05
     */
    private String respMessage;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMessage() {
        return respMessage;
    }

    public void setRespMessage(String respMessage) {
        this.respMessage = respMessage;
    }
}
