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

package com.taotao.cloud.realtime.datalake.mall.common;

/**
 *
 * Date: 2021/2/24
 * Desc: 电商业务常量
 */
public class GmallConstant {
    // 10 单据状态
    public static final String ORDER_STATUS_UNPAID = "1001"; // 未支付
    public static final String ORDER_STATUS_PAID = "1002"; // 已支付
    public static final String ORDER_STATUS_CANCEL = "1003"; // 已取消
    public static final String ORDER_STATUS_FINISH = "1004"; // 已完成
    public static final String ORDER_STATUS_REFUND = "1005"; // 退款中
    public static final String ORDER_STATUS_REFUND_DONE = "1006"; // 退款完成

    // 11 支付状态
    public static final String PAYMENT_TYPE_ALIPAY = "1101"; // 支付宝
    public static final String PAYMENT_TYPE_WECHAT = "1102"; // 微信
    public static final String PAYMENT_TYPE_UNION = "1103"; // 银联

    // 12 评价
    public static final String APPRAISE_GOOD = "1201"; // 好评
    public static final String APPRAISE_SOSO = "1202"; // 中评
    public static final String APPRAISE_BAD = "1203"; //  差评
    public static final String APPRAISE_AUTO = "1204"; // 自动

    // 13 退货原因
    public static final String REFUND_REASON_BAD_GOODS = "1301"; // 质量问题
    public static final String REFUND_REASON_WRONG_DESC = "1302"; // 商品描述与实际描述不一致
    public static final String REFUND_REASON_SALE_OUT = "1303"; //   缺货
    public static final String REFUND_REASON_SIZE_ISSUE = "1304"; //  号码不合适
    public static final String REFUND_REASON_MISTAKE = "1305"; //  拍错
    public static final String REFUND_REASON_NO_REASON = "1306"; //  不想买了
    public static final String REFUND_REASON_OTHER = "1307"; //    其他

    // 14 购物券状态
    public static final String COUPON_STATUS_UNUSED = "1401"; //    未使用
    public static final String COUPON_STATUS_USING = "1402"; //     使用中
    public static final String COUPON_STATUS_USED = "1403"; //       已使用

    // 15退款类型
    public static final String REFUND_TYPE_ONLY_MONEY = "1501"; //   仅退款
    public static final String REFUND_TYPE_WITH_GOODS = "1502"; //    退货退款

    // 24来源类型
    public static final String SOURCE_TYPE_QUREY = "2401"; //   用户查询
    public static final String SOURCE_TYPE_PROMOTION = "2402"; //   商品推广
    public static final String SOURCE_TYPE_AUTO_RECOMMEND = "2403"; //   智能推荐
    public static final String SOURCE_TYPE_ACTIVITY = "2404"; //   促销活动

    // 购物券范围
    public static final String COUPON_RANGE_TYPE_CATEGORY3 = "3301"; //
    public static final String COUPON_RANGE_TYPE_TRADEMARK = "3302"; //
    public static final String COUPON_RANGE_TYPE_SPU = "3303"; //

    // 购物券类型
    public static final String COUPON_TYPE_MJ = "3201"; // 满减
    public static final String COUPON_TYPE_DZ = "3202"; // 满量打折
    public static final String COUPON_TYPE_DJ = "3203"; //  代金券

    public static final String ACTIVITY_RULE_TYPE_MJ = "3101";
    public static final String ACTIVITY_RULE_TYPE_DZ = "3102";
    public static final String ACTIVITY_RULE_TYPE_ZK = "3103";

    // 关键词类型
    public static final String KEYWORD_SEARCH = "SEARCH";
    public static final String KEYWORD_CLICK = "CLICK";
    public static final String KEYWORD_CART = "CART";
    public static final String KEYWORD_ORDER = "ORDER";
}
