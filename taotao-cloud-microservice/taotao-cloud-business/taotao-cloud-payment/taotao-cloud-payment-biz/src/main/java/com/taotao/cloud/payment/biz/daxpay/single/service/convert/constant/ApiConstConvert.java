package com.taotao.cloud.payment.biz.daxpay.single.service.convert.constant;

import com.taotao.cloud.payment.biz.daxpay.service.entity.constant.ApiConst;
import com.taotao.cloud.payment.biz.daxpay.service.result.constant.ApiConstResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/7/14
 */
@Mapper
public interface ApiConstConvert {
    ApiConstConvert CONVERT = Mappers.getMapper(ApiConstConvert.class);

    ApiConstResult toResult(ApiConst source);
}
