/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.wechat.biz.wechat.core.menu.convert;

import cn.bootx.starter.wechat.core.menu.domin.WeChatMenuInfo;
import cn.bootx.starter.wechat.core.menu.entity.WeChatMenu;
import cn.bootx.starter.wechat.dto.menu.WeChatMenuDto;
import cn.bootx.starter.wechat.param.menu.WeChatMenuParam;
import me.chanjar.weixin.common.bean.menu.WxMenu;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 微信自定义菜单
 *
 * @author xxm
 * @since 2022-08-08
 */
@Mapper
public interface WeChatMenuConvert {
    WeChatMenuConvert CONVERT = Mappers.getMapper(WeChatMenuConvert.class);

    WeChatMenu convert(WeChatMenuParam in);

    WeChatMenuDto convert(WeChatMenu in);

    WeChatMenuInfo convert(WxMenu in);

    WxMenu convert(WeChatMenuInfo in);
}
