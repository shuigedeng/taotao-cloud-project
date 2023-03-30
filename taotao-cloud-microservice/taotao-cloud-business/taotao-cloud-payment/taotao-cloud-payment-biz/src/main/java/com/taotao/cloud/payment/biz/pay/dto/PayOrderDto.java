/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

    /** 平台订单号 */
    private String orderNo;

    /** 订单金额 */
    @ApiModelProperty(value = "订单金额")
    private BigDecimal amount;

    /** 商品id,注:转账付款没有ID */
    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    /** 商品标题 */
    @ApiModelProperty(value = "商品标题")
    private String goodsTitle;

    /** 订单备注 */
    @ApiModelProperty(value = "订单备注")
    private String remark;

    /** 用户付款中途退出返回商户网站的地址 */
    @ApiModelProperty(value = "用户付款中途退出返回商户网站的地址")
    private String quitUrl;

    /** 第三方渠道名称 */
    @ApiModelProperty(value = "第三方渠道名称")
    private String channelId;

    /** 第三方渠道用户openId */
    @ApiModelProperty(value = "第三方渠道用户openId")
    private String openId;

    /** 预留字段:第三方授权码 */
    @ApiModelProperty(value = "第三方授权码")
    private String code;
}
