package com.taotao.cloud.payment.biz.daxpay.single.service.entity.config;

import com.taotao.cloud.payment.biz.daxpay.service.common.entity.MchAppBaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.*;

/**
 * 商户应用消息通知配置
 * @author xxm
 * @since 2024/7/30
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_merchant_notify_config")
public class MerchantNotifyConfig extends MchAppBaseEntity {

    /** 消息通知类型编码 */
    private String code;

    /** 是否订阅 */
    private boolean subscribe;
}
