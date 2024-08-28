package com.taotao.cloud.iot.biz.communication.tcp.handler;


/**
 * TCP消息处理接口
 *
 * @author LSF maku_lsf@163.com
 */
public interface TCPMessageHandler {

    /**
     * 是否支持处理指定的topic
     *
     * @param topic
     * @return
     */
    boolean supports(String topic);

    /**
     * TCP消息处理接口
     *
     * @param topic
     * @param message
     */
    void handle(String topic, Object message);
}
