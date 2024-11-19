package com.taotao.cloud.payment.biz.daxpay.single.service.convert.constant;

import com.taotao.cloud.payment.biz.daxpay.service.entity.constant.ChannelConst;
import com.taotao.cloud.payment.biz.daxpay.service.result.constant.ChannelConstResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 *
 * @author xxm
 * @since 2024/7/14
 */
@Mapper
public interface ChannelConstConvert {
    ChannelConstConvert CONVERT = Mappers.getMapper(ChannelConstConvert.class);

    ChannelConstResult toResult(ChannelConst source);
}
