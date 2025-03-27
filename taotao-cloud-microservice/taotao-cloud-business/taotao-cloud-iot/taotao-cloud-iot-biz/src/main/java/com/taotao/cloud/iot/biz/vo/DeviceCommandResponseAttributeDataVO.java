package com.taotao.cloud.iot.biz.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.experimental.*;
import com.taotao.cloud.iot.biz.enums.DeviceCommandEnum;

import java.io.Serializable;

/**
 * 设备命令响应数据
 *
 * @author 
 */
@Data
@Schema(description = "设备命令响应VO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceCommandResponseAttributeDataVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "设备ID")
    private Long deviceId;

    @Schema(description = "命令类型", required = true)
    private DeviceCommandEnum command;

    @Schema(description = "命令ID", required = true)
    private String commandId;

    @Schema(description = "命令是否完成（默认true：命令已完成；false：命令未完成，后续命令完成将再次发送响应消息，服务端将继续等待该命令完成的响应）")
    private boolean isCompleted = true;

    @Schema(description = "响应状态码，0成功，其它数值异常，根据业务需要自定义")
    private Integer statusCode = 0;

    @Schema(description = "响应状态消息")
    private String statusMessage;

    @Schema(description = "命令响应结果")
    private String responsePayload;
}
