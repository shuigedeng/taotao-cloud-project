package com.taotao.cloud.iot.biz.communication.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.taotao.cloud.iot.biz.enums.DeviceCommandEnum;

/**
 * 设备命令响应DTO
 *
 * @author 
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Schema(description = "设备命令响应DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceCommandResponseDTO extends BaseCommandResponseDTO {
    /**
     * 命令类型
     */
    @Schema(description = "命令类型", required = true)
    private DeviceCommandEnum command;

    /**
     * 命令是否完成（默认true：命令已完成；false：命令未完成，后续命令完成将再次发送响应消息，服务端将继续等待该命令完成的响应）
     */
    @Schema(description = "命令是否完成（默认true：命令已完成；false：命令未完成，后续命令完成将再次发送响应消息，服务端将继续等待该命令完成的响应）")
    private boolean isCompleted = true;

    /**
     * 响应状态码，0成功，其它数值异常，根据业务需要自定义
     */
    @Schema(description = "响应状态码，0成功，其它数值异常，根据业务需要自定义")
    private Integer statusCode = 0;
    /**
     * 命令响应结果
     */
    @Schema(description = "命令响应结果")
    private String responsePayload;
}
