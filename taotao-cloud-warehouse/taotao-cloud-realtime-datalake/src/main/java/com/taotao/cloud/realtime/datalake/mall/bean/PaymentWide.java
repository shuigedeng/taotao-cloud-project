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

import com.taotao.cloud.realtime.mall.bean.OrderWide;
import com.taotao.cloud.realtime.mall.bean.PaymentInfo;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * Desc: 支付宽表实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWide {
    Long payment_id;
    String subject;
    String payment_type;
    String payment_create_time;
    String callback_time;
    Long detail_id;
    Long order_id;
    Long sku_id;
    BigDecimal order_price;
    Long sku_num;
    String sku_name;
    Long province_id;
    String order_status;
    Long user_id;
    BigDecimal total_amount;
    BigDecimal activity_reduce_amount;
    BigDecimal coupon_reduce_amount;
    BigDecimal original_total_amount;
    BigDecimal feight_fee;
    BigDecimal split_feight_fee;
    BigDecimal split_activity_amount;
    BigDecimal split_coupon_amount;
    BigDecimal split_total_amount;
    String order_create_time;

    String province_name; // 查询维表得到
    String province_area_code;
    String province_iso_code;
    String province_3166_2_code;
    Integer user_age;
    String user_gender;

    Long spu_id; // 作为维度数据 要关联进来
    Long tm_id;
    Long category3_id;
    String spu_name;
    String tm_name;
    String category3_name;

    public PaymentWide(PaymentInfo paymentInfo, OrderWide orderWide) {
        mergeOrderWide(orderWide);
        mergePaymentInfo(paymentInfo);
    }

    public void mergePaymentInfo(PaymentInfo paymentInfo) {
        if (paymentInfo != null) {
            try {
                BeanUtils.copyProperties(this, paymentInfo);
                payment_create_time = paymentInfo.create_time;
                payment_id = paymentInfo.id;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void mergeOrderWide(OrderWide orderWide) {
        if (orderWide != null) {
            try {
                BeanUtils.copyProperties(this, orderWide);
                order_create_time = orderWide.create_time;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
