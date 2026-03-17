package com.taotao.cloud.payment.biz.infrastructure.daxpay.single.service.entity.constant;

import cn.bootx.platform.common.mybatisplus.base.MpIdEntity;
import cn.bootx.platform.common.mybatisplus.function.ToResult;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.payment.biz.daxpay.service.convert.constant.ApiConstConvert;
import com.taotao.cloud.payment.biz.daxpay.service.result.constant.ApiConstResult;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 支付接口常量
 * @author xxm
 * @since 2024/7/14
 */
@EqualsAndHashCode(callSuper = true)
@Data

@TableName("pay_api_const")
public class ApiConst extends MpIdEntity implements ToResult<ApiConstResult> {

    /** 编码 */
    private String code;

    /** 接口名称 */
    private String name;

    /** 接口地址 */
    private String api;

    /** 是否启用 */
    private boolean enable;

    /** 备注 */
    private String remark;

    /**
     * 转换
     */
    @Override
    public ApiConstResult toResult() {
        return ApiConstConvert.CONVERT.toResult(this);
    }
}
