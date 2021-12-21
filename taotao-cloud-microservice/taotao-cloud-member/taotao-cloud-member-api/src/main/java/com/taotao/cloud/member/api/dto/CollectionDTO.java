package com.taotao.cloud.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 收藏数量变化DTO
 *
 *
 * @version v1.0 2021-11-30 10:14
 */
@Schema(description = "收藏数量变化DTO")
public class CollectionDTO {

	/**
	 * 变化的模型id 商品id/店铺id
	 */
	@Schema(description = "租户id")
	private String id;

	/**
	 * 变化的数量 -1 减少1 / +1 增加1
	 */
	@Schema(description = "租户id")
	private Integer num;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}
}
