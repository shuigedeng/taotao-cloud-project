package com.taotao.cloud.payment.biz.kit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * PaymentSuccessParams
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSuccessParams {

    /**
     * 支付方式
     */
    private String paymentMethod;
    /**
     * 第三方流水
     */
    private String receivableNo;

    /**
     * 支付金额
     */
    private Double price;

    /**
     * 支付参数
     */
    private PayParam payParam;
}
