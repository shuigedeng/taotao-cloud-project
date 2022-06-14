package com.taotao.cloud.payment.biz.bootx.core.paymodel.wechat.convert;

import cn.bootx.payment.core.paymodel.wechat.entity.WeChatPayConfig;
import cn.bootx.payment.core.paymodel.wechat.entity.WeChatPayment;
import cn.bootx.payment.dto.paymodel.wechat.WeChatPayConfigDto;
import cn.bootx.payment.dto.paymodel.wechat.WeChatPaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
* 微信转换类
* @author xxm
* @date 2021/6/21
*/
@Mapper
public interface WeChatConvert {
    WeChatConvert CONVERT = Mappers.getMapper(WeChatConvert.class);

    WeChatPayConfig convert(WeChatPayConfigDto in);

    WeChatPayConfigDto convert(WeChatPayConfig in);

    WeChatPaymentDto convert(WeChatPayment in);

    WeChatPayment convert(WeChatPaymentDto in);
}
