package com.taotao.cloud.iot.biz.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;

/**
 * 设备服务日志查询
 *
 * @author 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Schema(description = "设备服务日志查询")
public class IotDeviceServiceLogQuery extends Query {
    @Schema(description = "指令")
    private String deviceCommandEnum;

    @Schema(description = "设备id")
    private Long deviceId;
}
