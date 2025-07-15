package com.taotao.cloud.mq.client.consumer.constant;


import com.taotao.cloud.mq.common.dto.RespCode;

/**
 * @author shuigedeng
 * @since 2024.05
 */
public enum ConsumerRespCode implements RespCode {

    RPC_INIT_FAILED("C00001", "消费者启动失败"),
    SUBSCRIBE_FAILED("C00002", "消费者注册失败"),
    UN_SUBSCRIBE_FAILED("C00003", "消费者注销失败"),
    CONSUMER_STATUS_ACK_FAILED("C00004", "消费者状态回执失败"),
    CONSUMER_STATUS_ACK_BATCH_FAILED("C00005", "消费者状态批量回执失败"),
    ;

    private final String code;
    private final String msg;

    ConsumerRespCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
