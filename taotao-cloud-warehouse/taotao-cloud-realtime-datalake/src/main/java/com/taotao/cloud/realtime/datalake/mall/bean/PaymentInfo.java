package com.taotao.cloud.realtime.datalake.mall.bean;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * Date: 2021/2/20
 * Desc: 支付实体类
 */
@Data
public class PaymentInfo {
    Long id;
    Long order_id;
    Long user_id;
    BigDecimal total_amount;
    String subject;
    String payment_type;
    String create_time;
    String callback_time;
}

