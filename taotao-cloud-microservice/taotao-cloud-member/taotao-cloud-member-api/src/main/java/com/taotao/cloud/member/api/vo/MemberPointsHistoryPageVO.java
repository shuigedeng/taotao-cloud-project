
package com.taotao.cloud.member.api.vo;


import com.taotao.cloud.member.api.enums.PointTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 会员积分历史表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:26:14
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员积分历史VO")
public class MemberPointsHistoryPageVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "会员id")
	private String memberId;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "当前积分")
	private Long point;

	@Schema(description = "消费之前积分")
	private Long beforePoint;

	@Schema(description = "变动积分")
	private Long variablePoint;

	@Schema(description = "内容")
	private String content;

	/**
	 * @see PointTypeEnum
	 */
	@Schema(description = "积分类型")
	private String pointType;
}
