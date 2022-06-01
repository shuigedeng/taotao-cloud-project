package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员搜索VO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "会员搜索VO")
public class MemberRechargeVO implements Serializable {

	@Serial
	private static final long serialVersionUID = -7605952923416404638L;

	/**
	 * 充值订单编号
	 */
	private String rechargeSn;
	/**
	 * 会员id
	 */
	private Long memberId;
	/**
	 * 会员名称
	 */
	private String memberName;
	/**
	 * 充值金额
	 */
	private BigDecimal rechargeMoney;
	/**
	 * 充值方式，如：支付宝，微信不能为空
	 */
	private String rechargeWay;
	/**
	 * 支付状态
	 */
	private String payStatus;
	/**
	 * 支付插件id
	 */
	private String paymentPluginId;
	/**
	 * 第三方流水
	 */
	private String receivableNo;
	/**
	 * 支付时间
	 */
	private LocalDateTime payTime;

}
