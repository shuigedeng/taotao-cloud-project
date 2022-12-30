
package com.taotao.cloud.member.api.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;

/**
 * 会员积分历史表
 *
 * @param pointType
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:26:14
 */
@Schema(description = "会员积分历史VO")
public record MemberPointsHistoryPageVO(@Schema(description = "会员id") String memberId,
                                        @Schema(description = "会员名称") String memberName,
                                        @Schema(description = "当前积分") Long point,
                                        @Schema(description = "消费之前积分") Long beforePoint,
                                        @Schema(description = "变动积分") Long variablePoint,
                                        @Schema(description = "内容") String content,
                                        @Schema(description = "积分类型") String pointType) implements
	Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

}
