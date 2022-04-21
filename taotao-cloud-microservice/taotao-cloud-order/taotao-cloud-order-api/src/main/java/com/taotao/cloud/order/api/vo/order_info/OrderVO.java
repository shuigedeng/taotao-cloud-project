/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.order.api.vo.order_info;


import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单VO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 16:29:13
 */
@Schema(description = "订单VO")
public record OrderVO(
	/**
	 * id
	 */
	@Schema(description = "id")
	Long id,
	/**
	 * 买家ID
	 */
	@Schema(description = "买家ID")
	Long memberId,
	/**
	 * 优惠券id
	 */
	@Schema(description = "优惠券id")
	Long couponId,
	/**
	 * 秒杀活动id
	 */
	@Schema(description = "秒杀活动id")
	Long seckillId,
	/**
	 * 订单编码
	 */
	@Schema(description = "订单编码")
	String code,
	/**
	 * 订单金额
	 */
	@Schema(description = "订单金额")
	BigDecimal amount,
	/**
	 * 优惠金额
	 */
	@Schema(description = "优惠金额")
	BigDecimal discountAmount,
	/**
	 * 实际支付金额
	 */
	@Schema(description = "实际支付金额")
	BigDecimal actualAmount,
	/**
	 * 支付时间
	 */
	@Schema(description = "支付时间")
	LocalDateTime paySuccessTime,
	/**
	 * 订单主状态
	 */
	@Schema(description = "订单主状态")
	Integer mainStatus,
	/**
	 * 订单子状态
	 */
	@Schema(description = "订单子状态")
	Integer childStatus,
	/**
	 * 售后主状态
	 */
	@Schema(description = "售后主状态")
	Integer refundMainStatus,
	/**
	 * 售后子状态
	 */
	@Schema(description = "售后子状态")
	Integer refundChildStatus,
	/**
	 * 是否可评价 0-不可评价 1-可评价 2-可追评
	 */
	@Schema(description = "是否可评价")
	Integer evaluateStatus,
	/**
	 * 申请售后code
	 */
	@Schema(description = "申请售后code")
	String refundCode,
	/**
	 * 申请售后是否撤销
	 */
	@Schema(description = "申请售后是否撤销")
	Boolean hasCancel,
	/**
	 * 发货时间
	 */
	@Schema(description = "发货时间")
	LocalDateTime shipTime,
	/**
	 * 收货时间
	 */
	@Schema(description = "收货时间")
	LocalDateTime receiptTime,
	/**
	 * 交易结束时间
	 */
	@Schema(description = "交易结束时间")
	LocalDateTime tradeEndTime,
	/**
	 * 交易结束时间
	 */
	@Schema(description = "交易结束时间")
	String receiverName,
	/**
	 * 收货人电话
	 */
	@Schema(description = "收货人电话")
	String receiverPhone,
	/**
	 * 收货地址:json的形式存储
	 */
	@Schema(description = "收货地址:json的形式存储")
	String receiverAddressJson,
	/**
	 * 冗余收货地址字符串
	 */
	@Schema(description = "冗余收货地址字符串")
	String receiverAddress,
	/**
	 * 买家留言
	 */
	@Schema(description = "买家留言")
	String memberMsg,
	/**
	 * 取消订单说明
	 */
	@Schema(description = "取消订单说明")
	String cancelMsg,
	/**
	 * 物流公司code
	 */
	@Schema(description = "物流公司code")
	String expressCode,
	/**
	 * 物流公司名称
	 */
	@Schema(description = "物流公司名称")
	String expressName,
	/**
	 * 物流单号
	 */
	@Schema(description = "物流单号")
	String expressNumber,
	/**
	 * 买家IP
	 */
	@Schema(description = "买家IP")
	String memberIp,
	/**
	 * 是否结算
	 */
	@Schema(description = "是否结算")
	Boolean hasSettlement,
	/**
	 * 订单类型
	 */
	@Schema(description = "订单类型")
	Integer type,
	/**
	 * 条形码
	 */
	@Schema(description = "条形码")
	String barCode,
	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	LocalDateTime createTime,
	/**
	 * 最后修改时间
	 */
	@Schema(description = "最后修改时间")
	LocalDateTime lastModifiedTime) implements Serializable {

	static final long serialVersionUID = 5126530068827085130L;

}
