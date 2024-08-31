package com.taotao.cloud.iot.biz.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 设备指令
 *
 * @author 
 */
@Data
@Schema(description = "设备指令VO")
public class DeviceCommandVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "指令")
    private String command;

    @Schema(description = "指令内容")
    private String payload;
}
