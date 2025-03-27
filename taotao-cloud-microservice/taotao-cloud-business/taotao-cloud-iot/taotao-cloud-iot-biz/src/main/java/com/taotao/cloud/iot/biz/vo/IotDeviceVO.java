package com.taotao.cloud.iot.biz.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 设备表
 *
 * @author 
 */
@Data
@Schema(description = "设备表")
public class IotDeviceVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "ID")
    private Long id;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "设备类型，1.手持设备，2.柜体，3传感设备")
    private Integer type;

    @Schema(description = "协议类型")
    private String protocolType;

    @Schema(description = "唯一标识码")
    private String uid;

    @Schema(description = "设备密钥")
    private String secret;

    @Schema(description = "App版本号")
    private String appVersion;

    @Schema(description = "电池电量百分比")
    private String batteryPercent;

    @Schema(description = "温度")
    private String temperature;

    @Schema(description = "状态，0禁用，1正常")
    private Integer status;

    @Schema(description = "运行状态")
    private Integer runningStatus;

    @Schema(description = "上线时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime upTime;

    @Schema(description = "下线时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime downTime;

    @Schema(description = "租户ID")
    private Long tenantId;

    @Schema(description = "创建者")
    private Long creator;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime createTime;

    @Schema(description = "更新者")
    private Long updater;

    @Schema(description = "更新时间")
    @JsonFormat(pattern = DateUtils.DATE_TIME_PATTERN)
    private LocalDateTime updateTime;
    
}
