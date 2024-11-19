package com.taotao.cloud.payment.biz.daxpay.single.service.convert.constant;

import com.taotao.cloud.payment.biz.daxpay.service.entity.constant.MerchantNotifyConst;
import com.taotao.cloud.payment.biz.daxpay.service.result.constant.MerchantNotifyConstResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/8/5
 */
@Mapper
public interface MerchantNotifyConstConvert {
    MerchantNotifyConstConvert CONVERT = Mappers.getMapper(MerchantNotifyConstConvert.class);

    MerchantNotifyConstResult toResult(MerchantNotifyConst model);
}
