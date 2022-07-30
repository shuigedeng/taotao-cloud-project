package com.taotao.cloud.wechat.biz.wechat.core.notice.convert;

import cn.bootx.starter.wechat.core.notice.entity.WeChatTemplate;
import me.chanjar.weixin.mp.bean.template.WxMpTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
*
* @author xxm
* @date 2022/7/17
*/
@Mapper
public interface WeChatTemplateConvert {
    WeChatTemplateConvert CONVERT = Mappers.getMapper(WeChatTemplateConvert.class);

    WeChatTemplate convert(WxMpTemplate in);
}
