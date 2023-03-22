package com.taotao.cloud.log.biz.other.server.application.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UrlRequest {

	@ApiModelProperty("单次请求的id")
	private String requestId;

	@ApiModelProperty("设备id")
	private String deviceId;

	@ApiModelProperty("用户id")
	private String userId;

	@ApiModelProperty("长域名地址")
	private String longUrl;

	@ApiModelProperty("短域名地址")
	private String shortUrl;
}
