package com.taotao.cloud.iot.biz.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeviceServiceEnum {

    COMMAND_ID("命令ID");

    /**
     * 类型值
     */
    private final String value;


}
