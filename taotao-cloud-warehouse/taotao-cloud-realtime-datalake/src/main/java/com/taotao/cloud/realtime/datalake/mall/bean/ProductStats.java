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

package com.taotao.cloud.realtime.datalake.mall.bean;

/**
 *
 * Date: 2021/2/23
 *
 * @Builder注解 可以使用构造者方式创建对象，给属性赋值
 *
 * 在使用构造者方式给属性赋值的时候，属性的初始值会丢失 该注解的作用就是修复这个问题 例如：我们在属性上赋值了初始值为0L，如果不加这个注解，通过构造者创建的对象属性值会变为null
 */

import com.taotao.cloud.realtime.mall.bean.TransientSink;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

/**
 * ProductStats
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Data
@Builder
public class ProductStats {

    String stt; // 窗口起始时间
    String edt; // 窗口结束时间
    Long sku_id; // sku编号
    String sku_name; // sku名称
    BigDecimal sku_price; // sku单价
    Long spu_id; // spu编号
    String spu_name; // spu名称
    Long tm_id; // 品牌编号
    String tm_name; // 品牌名称
    Long category3_id; // 品类编号
    String category3_name; // 品类名称

    Long display_ct = 0L; // 曝光数

    Long click_ct = 0L; // 点击数

    Long favor_ct = 0L; // 收藏数

    Long cart_ct = 0L; // 添加购物车数

    Long order_sku_num = 0L; // 下单商品个数

    // 下单商品金额
    BigDecimal order_amount = BigDecimal.ZERO;

    Long order_ct = 0L; // 订单数

    // 支付金额
    BigDecimal payment_amount = BigDecimal.ZERO;

    Long paid_order_ct = 0L; // 支付订单数

    Long refund_order_ct = 0L; // 退款订单数

    BigDecimal refund_amount = BigDecimal.ZERO;

    Long comment_ct = 0L; // 评论数

    Long good_comment_ct = 0L; // 好评数

    @TransientSink
    Set orderIdSet = new HashSet(); // 用于统计订单数

    @TransientSink
    Set paidOrderIdSet = new HashSet(); // 用于统计支付订单数

    @TransientSink
    Set refundOrderIdSet = new HashSet(); // 用于退款支付订单数

    Long ts; // 统计时间戳
}
