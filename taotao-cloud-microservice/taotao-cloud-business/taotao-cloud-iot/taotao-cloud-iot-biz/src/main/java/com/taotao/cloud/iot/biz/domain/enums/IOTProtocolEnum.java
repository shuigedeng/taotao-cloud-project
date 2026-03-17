package com.taotao.cloud.iot.biz.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * IOT常用的通信协议
 *
 * @author 
 */
@Getter
@RequiredArgsConstructor
public enum IOTProtocolEnum {

    MQTT("MQTT"),
    TCP("TCP"),
    UDP("UDP"),
    BLE("BLE"),
    CoAP("CoAP"),
    LwM2M("LwM2M"),
    Modbus("Modbus");

    private final String value;
}

