package com.taotao.cloud.log.biz.other.server.application.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UrlResponse {

	private String requestId;

	@ApiModelProperty("描述信息")
	private String Description;

	@ApiModelProperty("长域名地址")
	private String longUrl;

	@ApiModelProperty("短域名地址")
	private String shortUrl;

	@ApiModelProperty("错误信息, 0表示正常")
	private String errMsg = "0";

	public UrlResponse() {
	}

	;

	public UrlResponse(String requestId, String description, String longUrl, String shortUrl) {
		this.requestId = requestId;
		this.Description = description;
		this.longUrl = longUrl;
		this.shortUrl = shortUrl;
	}

	;

}
