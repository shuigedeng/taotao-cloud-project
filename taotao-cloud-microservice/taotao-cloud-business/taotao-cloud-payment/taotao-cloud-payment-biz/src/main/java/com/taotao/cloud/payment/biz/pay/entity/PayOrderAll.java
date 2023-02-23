/*
 * MIT License
 * Copyright <2021-2022>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE
 * OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 * @Author: Sinda
 * @Email:  xhuicloud@163.com
 */

package com.taotao.cloud.payment.biz.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * 订单表
 */
@Data
@ApiModel(value = "订单")
@TableName(value = "pay_order_all")
public class PayOrderAll implements Serializable {

	/**
	 * 订单表id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "订单表id")
	private Integer id;

	/**
	 * 订单金额
	 */
	@ApiModelProperty(value = "订单金额")
	private BigDecimal amount;

	/**
	 * 商品id,注:转账付款没有ID
	 */
	@ApiModelProperty(value = "商品id")
	private Integer goodsId;

	/**
	 * 商品标题
	 */
	@ApiModelProperty(value = "商品标题")
	private String goodsTitle;

	/**
	 * 平台订单号
	 */
	@ApiModelProperty(value = "平台订单号")
	private String orderNo;

	/**
	 * 平台退款单号
	 */
	@ApiModelProperty(value = "平台退款单号")
	private String refOrderNo;

	/**
	 * 平台支付单号
	 */
	@ApiModelProperty(value = "平台支付单号")
	private String payOrderNo;

	/**
	 * 第三方预支付订单号
	 */
	@ApiModelProperty(value = "第三方预支付订单号")
	private String thirdPreNo;

	/**
	 * 第三方渠道订单号
	 */
	@ApiModelProperty(value = "第三方渠道订单号")
	private String thirdOrderNo;

	/**
	 * 第三方渠道退款单号
	 */
	@ApiModelProperty(value = "第三方渠道退款单号")
	private String thirdRefNo;

	/**
	 * 第三方渠道名称
	 */
	@ApiModelProperty(value = "第三方渠道名称")
	private String channelId;

	/**
	 * 第三方渠道用户openId
	 */
	@ApiModelProperty(value = "第三方渠道用户openId")
	private String openId;

	/**
	 * 状态:
	 */
	@ApiModelProperty(value = "状态:")
	private Integer state;

	/**
	 * 订单备注
	 */
	@ApiModelProperty(value = "订单备注")
	private String remark;

	/**
	 * 租户id
	 */
	@ApiModelProperty(value = "租户id")
	private Integer tenantId;

	/**
	 * 下单时间
	 */
	@ApiModelProperty(value = "下单时间")
	private LocalDateTime createTime;

	/**
	 * 支付时间
	 */
	@ApiModelProperty(value = "支付时间")
	private LocalDateTime payTime;

	/**
	 * 退款时间
	 */
	@ApiModelProperty(value = "退款时间")
	private LocalDateTime refTime;

	/**
	 * 完成时间
	 */
	@ApiModelProperty(value = "完成时间")
	private LocalDateTime finishTime;

	private static final long serialVersionUID = 1L;
}
