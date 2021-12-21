package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 会员积分VO
 *
 *
 * @since 2021/2/25 9:52 上午
 */
@Schema(description = "会员积分VO")
public class MemberPointsHistoryVO {

	@Schema(description = "当前会员积分")
	private Long point;

	@Schema(description = "累计获得积分")
	private Long totalPoint;


	public MemberPointsHistoryVO() {
		this.point = 0L;
		this.totalPoint = 0L;
	}

	public Long getPoint() {
		return point;
	}

	public void setPoint(Long point) {
		this.point = point;
	}

	public Long getTotalPoint() {
		return totalPoint;
	}

	public void setTotalPoint(Long totalPoint) {
		this.totalPoint = totalPoint;
	}
}
