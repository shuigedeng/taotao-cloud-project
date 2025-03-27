package com.taotao.cloud.iot.biz.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import com.taotao.cloud.iot.biz.enums.DeviceCommandEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备服务日志
 *
 * @author 
 */
@Data
@Schema(description = "设备服务日志")
public class IotDeviceServiceLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "服务类型")
    private Integer serviceType;

    @Schema(description = "指令")
    private DeviceCommandEnum deviceCommandEnum;

    @Schema(description = "服务标识id")
    private String serviceUid;

    @Schema(description = "服务数据")
    private String servicePayload;

    @Schema(description = "服务时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime serviceTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime createTime;
}
