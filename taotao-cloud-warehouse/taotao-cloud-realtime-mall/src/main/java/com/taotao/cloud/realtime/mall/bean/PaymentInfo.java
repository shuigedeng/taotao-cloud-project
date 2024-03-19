package com.taotao.cloud.realtime.mall.bean;

import java.math.BigDecimal;
import lombok.Data;

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

