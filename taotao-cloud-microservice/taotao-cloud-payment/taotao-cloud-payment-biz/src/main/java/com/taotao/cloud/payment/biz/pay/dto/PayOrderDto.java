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

package com.taotao.cloud.payment.biz.pay.dto;

import io.swagger.annotations.ApiModelProperty;
import java.math.BigDecimal;
import lombok.Data;

/**
 * @program: XHuiCloud
 * @description: PayOrderDto
 * @author: Sinda
 * @create: 2020-06-09 10:17
 */
@Data
public class PayOrderDto {

	/**
	 * 平台订单号
	 */
	private String orderNo;

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
	 * 订单备注
	 */
	@ApiModelProperty(value = "订单备注")
	private String remark;

	/**
	 * 用户付款中途退出返回商户网站的地址
	 */
	@ApiModelProperty(value = "用户付款中途退出返回商户网站的地址")
	private String quitUrl;

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
	 * 预留字段:第三方授权码
	 */
	@ApiModelProperty(value = "第三方授权码")
	private String code;

}
