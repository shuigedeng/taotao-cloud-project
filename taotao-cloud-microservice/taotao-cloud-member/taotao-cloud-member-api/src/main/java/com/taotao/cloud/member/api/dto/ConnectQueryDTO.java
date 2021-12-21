package com.taotao.cloud.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * ConnectQueryDTO
 *
 * 
 * @version v1.0 2021-12-01 14:34
 */
@Schema(description = "ConnectQueryDTO")
public class ConnectQueryDTO {

	/**
	 * 用户id
	 */
	@Schema(description = "租户id")
	private String userId;

	/**
	 * 第三方id
	 */
	@Schema(description = "租户id")
	private String unionId;

	/**
	 * 联合登陆类型
	 */
	@Schema(description = "租户id")
	private String unionType;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

	public String getUnionType() {
		return unionType;
	}

	public void setUnionType(String unionType) {
		this.unionType = unionType;
	}
}
