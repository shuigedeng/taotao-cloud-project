package com.taotao.cloud.iot.biz.infrastructure.communication.mqtt.handler;


import com.taotao.cloud.iot.biz.infrastructure.communication.dto.DevicePropertyDTO;

/**
 * 设备属性变化处理器
 *
 * @author 
 */
public interface DevicePropertyChangeHandler {
    /**
     * 设备属性状态变化处理
     *
     * @param topic
     * @param deviceStatus
     */
    void handle(String topic, DevicePropertyDTO deviceStatus);
}
