package com.taotao.cloud.member.api.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 会员评价VO
 */
@Schema(description = "会员评价VO")
public record MemberEvaluationListVO(@Schema(description = "评论ID") String id,
                                     @Schema(description = "会员名称") String memberName,
                                     @Schema(description = "商品名称") String goodsName,
                                     @Schema(description = "好中差评", allowableValues = "GOOD,NEUTRAL,BAD") String grade,
                                     @Schema(description = "评价内容") String content,
                                     @Schema(description = "状态 ", allowableValues = " OPEN 正常 ,CLOSE 关闭") String status,
                                     @Schema(description = "回复状态") Boolean replyStatus,
                                     @Schema(description = "创建时间") @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime createTime,
                                     @Schema(description = "物流评分") Integer deliveryScore,
                                     @Schema(description = "服务评分") Integer serviceScore,
                                     @Schema(description = "描述评分") Integer descriptionScore) implements
	Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

}
