package com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.cash.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.cash.entity.CashConfig;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.cash.entity.CashRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.cash.CashPayConfigDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.cash.CashRecordDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/2/17
 */
@Mapper
public interface CashPayConfigConvert {
    CashPayConfigConvert CONVERT = Mappers.getMapper(CashPayConfigConvert.class);

    CashPayConfigDto convert(CashConfig in);

    CashRecordDto convert(CashRecord in);

}
