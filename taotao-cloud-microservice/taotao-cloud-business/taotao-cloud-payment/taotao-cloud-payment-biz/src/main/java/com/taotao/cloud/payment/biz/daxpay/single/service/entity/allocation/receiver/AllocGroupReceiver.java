package com.taotao.cloud.payment.biz.daxpay.single.service.entity.allocation.receiver;

import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import com.taotao.cloud.payment.biz.daxpay.service.bo.allocation.AllocGroupReceiverResultBo;
import com.taotao.cloud.payment.biz.daxpay.service.common.entity.MchAppBaseEntity;
import com.taotao.cloud.payment.biz.daxpay.service.convert.allocation.AllocGroupReceiverConvert;

import java.math.BigDecimal;

/**
 * 分账组和接收者关系
 * @author xxm
 * @since 2024/6/4
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_alloc_group_receiver")
public class AllocGroupReceiver extends MchAppBaseEntity implements ToResult<AllocGroupReceiverResultBo> {

    /** 分账组ID */
    private Long groupId;

    /** 接收者ID */
    private Long receiverId;

    /** 分账比例(百分之多少) */
    private BigDecimal rate;

    @Override
    public AllocGroupReceiverResultBo toResult() {
        return AllocGroupReceiverConvert.CONVERT.convert(this);
    }
}
