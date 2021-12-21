package com.taotao.cloud.member.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 会员积分
 *
 * 
 * @since 2020/12/14 16:31
 */
@Schema(description = "租户id")
public class MemberPointMessage {


	@Schema(description = "积分")
	private Long point;

	@Schema(description = "是否增加积分")
	private String type;

	@Schema(description = "会员id")
	private String memberId;

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}
