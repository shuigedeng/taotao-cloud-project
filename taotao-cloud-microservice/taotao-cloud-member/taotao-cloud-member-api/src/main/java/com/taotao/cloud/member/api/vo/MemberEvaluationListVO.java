package com.taotao.cloud.member.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 会员评价VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员评价VO")
public class MemberEvaluationListVO implements Serializable {

	private static final long serialVersionUID = -7605952923416404638L;

	@Schema(description = "评论ID")
	private String id;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "好中差评", allowableValues = "GOOD,NEUTRAL,BAD")
	private String grade;

	@Schema(description = "评价内容")
	private String content;

	@Schema(description = "状态 ", allowableValues = " OPEN 正常 ,CLOSE 关闭")
	private String status;

	@Schema(description = "回复状态")
	private Boolean replyStatus;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	@Schema(description = "物流评分")
	private Integer deliveryScore;

	@Schema(description = "服务评分")
	private Integer serviceScore;

	@Schema(description = "描述评分")
	private Integer descriptionScore;
}
