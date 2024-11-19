package com.taotao.cloud.payment.biz.daxpay.single.service.convert.constant;

import com.taotao.cloud.payment.biz.daxpay.service.entity.constant.MethodConst;
import com.taotao.cloud.payment.biz.daxpay.service.result.constant.MethodConstResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/7/14
 */
@Mapper
public interface MethodConstConvert {
    MethodConstConvert CONVERT = Mappers.getMapper(MethodConstConvert.class);

    MethodConstResult toResult(MethodConst source);
}
