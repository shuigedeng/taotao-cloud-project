package com.taotao.cloud.payment.biz.daxpay.single.service.convert.notice;

import com.taotao.cloud.payment.biz.daxpay.service.entity.notice.callback.MerchantCallbackRecord;
import com.taotao.cloud.payment.biz.daxpay.service.entity.notice.callback.MerchantCallbackTask;
import com.taotao.cloud.payment.biz.daxpay.service.result.notice.callback.MerchantCallbackRecordResult;
import com.taotao.cloud.payment.biz.daxpay.service.result.notice.callback.MerchantCallbackTaskResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 商户回调消息
 * @author xxm
 * @since 2024/8/5
 */
@Mapper
public interface MerchantCallbackConvert {

    MerchantCallbackConvert CONVERT = Mappers.getMapper(MerchantCallbackConvert.class);

    MerchantCallbackRecordResult toResult(MerchantCallbackRecord in);

    MerchantCallbackTaskResult toResult(MerchantCallbackTask in);
}
