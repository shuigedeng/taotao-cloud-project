package com.taotao.cloud.wechat.biz.wechat.core.user.convert;

import cn.bootx.starter.wechat.core.user.entity.WechatFans;
import cn.bootx.starter.wechat.dto.user.WechatFansDto;
import cn.bootx.starter.wechat.param.user.WechatFansParam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 微信公众号粉丝
 * @author xxm
 * @date 2022-07-16
 */
@Mapper
public interface WechatFansConvert {
    WechatFansConvert CONVERT = Mappers.getMapper(WechatFansConvert.class);

    WechatFans convert(WechatFansParam in);

    WechatFansDto convert(WechatFans in);

}
