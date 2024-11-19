package com.taotao.cloud.payment.biz.daxpay.single.service.entity.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.taotao.cloud.payment.biz.daxpay.service.convert.constant.MethodConstConvert;
import com.taotao.cloud.payment.biz.daxpay.service.result.constant.MethodConstResult;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 支付方式常量
 * @see com.taotao.cloud.payment.biz.daxpay.core.enums.PayMethodEnum
 * @author xxm
 * @since 2024/6/26
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@TableName("pay_method_const")
public class MethodConst extends MpIdEntity implements ToResult<MethodConstResult> {

    /** 编码 */
    private String code;

    /** 名称 */
    private String name;

    /** 是否启用 */
    private boolean enable;

    /** 备注 */
    private String remark;

    /**
     * 转换
     */
    @Override
    public MethodConstResult toResult() {
        return MethodConstConvert.CONVERT.toResult(this);
    }
}
