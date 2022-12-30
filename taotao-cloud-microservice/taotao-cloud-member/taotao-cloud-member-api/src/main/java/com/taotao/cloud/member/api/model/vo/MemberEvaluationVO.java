package com.taotao.cloud.member.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 会员评价VO
 */
@Schema(description = "会员评价VO")
public record MemberEvaluationVO(@Schema(description = "会员ID") String memberId,
                                 @Schema(description = "会员名称") String memberName,
                                 @Schema(description = "会员头像") String memberProfile,
                                 @Schema(description = "店铺ID") String storeId,
                                 @Schema(description = "店铺名称") String storeName,
                                 @Schema(description = "商品ID") String goodsId,
                                 @Schema(description = "SKU_ID") String skuId,
                                 @Schema(description = "会员ID") String goodsName,
                                 @Schema(description = "商品图片") String goodsImage,
                                 @Schema(description = "订单号") String orderNo,
                                 @Schema(description = "好中差评 , GOOD：好评，MODERATE：中评，WORSE：差评") String grade,
                                 @Schema(description = "评价内容") String content,
                                 @Schema(description = "评价图片 逗号分割") String images,
                                 @Schema(description = "状态  OPEN 正常 ,CLOSE 关闭") String status,
                                 @Schema(description = "评论图片") String reply,
                                 @Schema(description = "评价回复图片") String replyImage,
                                 @Schema(description = "评论是否有图片 true 有 ,false 没有") Boolean haveImage,
                                 @Schema(description = "回复是否有图片 true 有 ,false 没有") Boolean haveReplyImage,
                                 @Schema(description = "回复状态") Boolean replyStatus,
                                 @Schema(description = "物流评分") Integer deliveryScore,
                                 @Schema(description = "服务评分") Integer serviceScore,
                                 @Schema(description = "描述评分") Integer descriptionScore,
                                 @Schema(description = "评论图片") List<String> evaluationImages,
                                 @Schema(description = "回复评论图片") List<String> replyEvaluationImages) implements
	Serializable {

	@Serial
	private static final long serialVersionUID = 6696978796248845481L;

}
