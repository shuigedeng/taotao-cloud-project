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

package com.taotao.cloud.payment.biz.kit.plugin.wechat.model;

import lombok.Data;
import lombok.experimental.Accessors;

/** 统一下单-商户门店信息 */
@Data
@Accessors(chain = true)
public class UnifiedOrderModel {
    /** 公众号ID */
    private String appid;
    /** 服务商公众号ID */
    private String sp_appid;
    /** 直连商户号 */
    private String mchid;
    /** 服务商户号 */
    private String sp_mchid;
    /** 子商户公众号ID */
    private String sub_appid;
    /** 子商户号 */
    private String sub_mchid;
    /** 商品描述 */
    private String description;
    /** 商户订单号 */
    private String out_trade_no;
    /** 交易结束时间 */
    private String time_expire;
    /** 附加数据 */
    private String attach;
    /** 通知地址 */
    private String notify_url;
    /** 订单优惠标记 */
    private String goods_tag;
    /** 结算信息 */
    private SettleInfo settle_info;
    /** 订单金额 */
    private Amount amount;
    /** 支付者 */
    private Payer payer;
    /** 优惠功能 */
    private Detail detail;
    /** 场景信息 */
    private SceneInfo scene_info;
}
