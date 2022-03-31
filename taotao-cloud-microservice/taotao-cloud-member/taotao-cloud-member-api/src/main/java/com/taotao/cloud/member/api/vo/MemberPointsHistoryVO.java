package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 会员积分VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
}
