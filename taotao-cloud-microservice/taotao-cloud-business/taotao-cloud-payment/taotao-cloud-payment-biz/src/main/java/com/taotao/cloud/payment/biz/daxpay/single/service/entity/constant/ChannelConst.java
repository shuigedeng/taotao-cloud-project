package com.taotao.cloud.payment.biz.daxpay.single.service.entity.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.taotao.cloud.payment.biz.daxpay.service.convert.constant.ChannelConstConvert;
import com.taotao.cloud.payment.biz.daxpay.service.result.constant.ChannelConstResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;
import lombok.experimental.*;

/**
 * 支付通道常量
 * @author xxm
 * @since 2024/6/25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_channel_const")
public class ChannelConst extends MpIdEntity implements ToResult<ChannelConstResult> {

    /** 通道编码 */
    private String code;

    /** 通道名称 */
    private String name;

    /** 是否启用 */
    private boolean enable;

    /** 备注 */
    private String remark;

    /**
     * 转换
     */
    @Override
    public ChannelConstResult toResult() {
        return ChannelConstConvert.CONVERT.toResult(this);
    }
}
