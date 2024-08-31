package com.taotao.cloud.iot.biz.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.taotao.cloud.iot.biz.enums.DeviceEventTypeEnum;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备事件日志
 *
 * @author 
 */
@Data
@Schema(description = "设备事件日志")
public class IotDeviceEventLogVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "事件类型")
    private Integer eventType;

    @Schema(description = "事件")
    private DeviceEventTypeEnum eventTypeEnum;

    @Schema(description = "事件标识id")
    private String eventUid;

    @Schema(description = "事件数据")
    private String eventPayload;

    @Schema(description = "事件时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime eventTime;

}
