package com.taotao.cloud.payment.biz.bootx.core.paymodel.cash.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.payment.biz.bootx.core.paymodel.base.entity.BasePayment;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
* 现金支付记录
* @author xxm
* @date 2021/6/23
*/
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("pay_cash_payment")
@Accessors(chain = true)
public class CashPayment extends BasePayment {
}
