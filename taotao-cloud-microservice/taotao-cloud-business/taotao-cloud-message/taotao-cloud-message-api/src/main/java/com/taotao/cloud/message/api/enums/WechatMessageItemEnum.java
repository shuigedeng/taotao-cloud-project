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

package com.taotao.cloud.message.api.enums;

/** 微信模版设置变量 */
public enum WechatMessageItemEnum {
    /** 商户名称 */
    SHOP_NAME(new String[] {"商户名称"}),
    /** 买家昵称 */
    MEMBER_NAME(new String[] {"买家昵称"}),
    /** 订单金额 */
    PRICE(new String[] {"支付金额", "订单金额"}),
    /** 订单详情 */
    GOODS_INFO(new String[] {"订单详情", "商品清单", "商品名称"}),
    /** 订单编号 */
    ORDER_SN(new String[] {"订单编号"}),
    /** 快递公司 */
    LOGISTICS_NAME(new String[] {"快递公司"}),
    /** 快递单号 */
    LOGISTICS_NO(new String[] {"快递单号"}),
    /** 发货时间 */
    LOGISTICS_TIME(new String[] {"发货时间"}),
    /** 支付时间 */
    PAYMENT_TIME(new String[] {"支付时间"});

    /** 名称 */
    private String[] text;

    WechatMessageItemEnum(String[] text) {
        this.text = text;
    }

    public String[] getText() {
        return text;
    }
}
