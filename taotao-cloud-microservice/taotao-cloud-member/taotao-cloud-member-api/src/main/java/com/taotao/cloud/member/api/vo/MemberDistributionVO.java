package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 会员分布VO
 *
 * 
 * @since 2021-02-26 17:25
 */
@Schema(description = "会员分布VO")
public class MemberDistributionVO {

	@Schema(description = "客户端类型")
	private String clientEnum;

	@Schema(description = "数量")
	private Integer num;

	@Schema(description = "比例")
	private Double proportion;

	public String getClientEnum() {
		return clientEnum;
	}

	public void setClientEnum(String clientEnum) {
		this.clientEnum = clientEnum;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getProportion() {
		return proportion;
	}

	public void setProportion(Double proportion) {
		this.proportion = proportion;
	}
}
