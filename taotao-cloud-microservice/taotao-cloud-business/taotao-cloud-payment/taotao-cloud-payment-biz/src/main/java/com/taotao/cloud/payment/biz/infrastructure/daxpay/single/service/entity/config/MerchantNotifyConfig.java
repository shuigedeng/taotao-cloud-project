package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.entity.config;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.payment.biz.daxpay.service.common.entity.MchAppBaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 商户应用消息通知配置
 * @author xxm
 * @since 2024/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data

@TableName("pay_merchant_notify_config")
public class MerchantNotifyConfig extends MchAppBaseEntity {

    /** 消息通知类型编码 */
    private String code;

    /** 是否订阅 */
    private boolean subscribe;
}
