package com.taotao.cloud.order.biz.entity;


import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单支付流水表
 *
 * @author dengtao
 * @date 2020/4/30 15:45
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
//@Entity
@Table(name = "tt_order_pay_seq")
@org.hibernate.annotations.Table(appliesTo = "tt_order_pay_seq", comment = "订单支付流水表")
public class OrderPaySeq extends BaseEntity {

    /**
     * 支付流水编码--需要与微信的预支付ID进行关联
     */
    private String payCode;

    /**
     * 买家ID
     */
    private Long customerId;

    /**
     * 付款方银行编码
     */
    private String payerBankCode;

    /**
     * 交易金额
     */
    private BigDecimal actualAmount = new BigDecimal(0);

    /**
     * 微信预支付ID
     */
    private String prepayId;

    /**
     * 微信交易ID
     */
    private String transactionId;

    /**
     * 微信商户ID
     */
    private String mchId;

    /**
     * 微信APPID
     */
    private String appId;

    /**
     * 状态 0-等待支付 1-超时关闭 2-支付失败 3-支付成功
     */
    private Short status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createDate = LocalDateTime.now();

}
