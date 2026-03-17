package com.taotao.cloud.iot.biz.infrastructure.communication.dto;

import com.taotao.cloud.iot.biz.enums.DevicePropertyEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 设备属性对象
 *
 * @author 
 */
@Data
@Schema(description = "设备属性对象")
public class DevicePropertyDTO extends BaseDeviceID {
    /**
     * 设备属性类型
     */
    @Schema(description = "设备属性类型")
    private DevicePropertyEnum propertyType;

    /**
     * 属性数据
     */
    @Schema(description = "状态数据，不同状态类型需传入相应的状态数据", required = true)
    private String payload;
}
