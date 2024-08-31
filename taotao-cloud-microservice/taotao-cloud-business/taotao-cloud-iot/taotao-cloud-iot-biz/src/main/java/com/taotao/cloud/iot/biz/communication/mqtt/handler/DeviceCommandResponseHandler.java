package com.taotao.cloud.iot.biz.communication.mqtt.handler;


import com.taotao.cloud.iot.biz.communication.dto.DeviceCommandResponseDTO;

/**
 * 设备命令响应处理器
 *
 * @author 
 */
public interface DeviceCommandResponseHandler {
    /**
     * 设备命令响应处理
     *
     * @param topic
     * @param commandResponse
     */
    void handle(String topic, DeviceCommandResponseDTO commandResponse);
}
