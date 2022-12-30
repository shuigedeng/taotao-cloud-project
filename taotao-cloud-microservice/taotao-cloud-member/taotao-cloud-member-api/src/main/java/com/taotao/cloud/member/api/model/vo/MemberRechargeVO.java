package com.taotao.cloud.member.api.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员搜索VO
 *
 * @param rechargeSn      充值订单编号
 * @param memberId        会员id
 * @param memberName      会员名称
 * @param rechargeMoney   充值金额
 * @param rechargeWay     充值方式，如：支付宝，微信不能为空
 * @param payStatus       支付状态
 * @param paymentPluginId 支付插件id
 * @param receivableNo    第三方流水
 * @param payTime         支付时间
 */
@Schema(description = "会员搜索VO")
public record MemberRechargeVO(@Schema(description = "充值订单编号") String rechargeSn,
                               @Schema(description = "会员id") Long memberId,
                               @Schema(description = "会员名称") String memberName,
                               @Schema(description = "充值金额") BigDecimal rechargeMoney,
                               @Schema(description = "充值方式，如：支付宝，微信不能为空") String rechargeWay,
                               @Schema(description = "支付状态") String payStatus,
                               @Schema(description = "支付插件id") String paymentPluginId,
                               @Schema(description = "第三方流水") String receivableNo,
                               @Schema(description = "支付时间") LocalDateTime payTime) implements
	Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

}
