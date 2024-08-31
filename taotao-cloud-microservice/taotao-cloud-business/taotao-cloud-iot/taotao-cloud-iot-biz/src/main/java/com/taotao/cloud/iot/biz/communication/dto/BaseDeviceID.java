package com.taotao.cloud.iot.biz.communication.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 设备ID
 *
 * @author 
 */
@Data
@Schema(description = "设备ID")
public class BaseDeviceID {

    @Schema(description = "设备ID")
    protected String deviceId;
}
