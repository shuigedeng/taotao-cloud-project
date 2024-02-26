package com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.convert;

import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.entity.WeChatPayConfig;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.channel.wechat.entity.WeChatPayRecord;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.wechat.WeChatPayConfigDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.dto.channel.wechat.WeChatPayRecordDto;
import com.taotao.cloud.payment.biz.daxpay.single.service.param.channel.wechat.WeChatPayConfigParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 微信转换类
 *
 * @author xxm
 * @since 2021/6/21
 */
@Mapper
public interface WeChatConvert {

    WeChatConvert CONVERT = Mappers.getMapper(WeChatConvert.class);

    WeChatPayConfig convert(WeChatPayConfigParam in);

    WeChatPayRecordDto convert(WeChatPayRecord in);

    WeChatPayConfigDto convert(WeChatPayConfig in);

}
