package com.taotao.cloud.payment.biz.daxpay.single.service.convert.notice;

import com.taotao.cloud.payment.biz.daxpay.service.entity.notice.notify.MerchantNotifyRecord;
import com.taotao.cloud.payment.biz.daxpay.service.entity.notice.notify.MerchantNotifyTask;
import com.taotao.cloud.payment.biz.daxpay.service.result.notice.notify.MerchantNotifyRecordResult;
import com.taotao.cloud.payment.biz.daxpay.service.result.notice.notify.MerchantNotifyTaskResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 商户订阅通知
 * @author xxm
 * @since 2024/8/5
 */
@Mapper
public interface MerchantNotifyConvert {

    MerchantNotifyConvert CONVERT = Mappers.getMapper(MerchantNotifyConvert.class);

    MerchantNotifyRecordResult toResult(MerchantNotifyRecord in);

    MerchantNotifyTaskResult toResult(MerchantNotifyTask in);
}
