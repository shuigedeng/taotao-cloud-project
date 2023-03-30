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

package com.taotao.cloud.wechat.biz.mp.convert.menu;

import cn.iocoder.yudao.module.mp.controller.admin.menu.vo.MpMenuRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.menu.vo.MpMenuSaveReqVO;
import cn.iocoder.yudao.module.mp.dal.dataobject.menu.MpMenuDO;
import cn.iocoder.yudao.module.mp.service.message.bo.MpMessageSendOutReqBO;
import java.util.List;
import me.chanjar.weixin.common.bean.menu.WxMenuButton;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MpMenuConvert {

    MpMenuConvert INSTANCE = Mappers.getMapper(MpMenuConvert.class);

    MpMenuRespVO convert(MpMenuDO bean);

    List<MpMenuRespVO> convertList(List<MpMenuDO> list);

    @Mappings({
        @Mapping(source = "menu.appId", target = "appId"),
        @Mapping(source = "menu.replyMessageType", target = "type"),
        @Mapping(source = "menu.replyContent", target = "content"),
        @Mapping(source = "menu.replyMediaId", target = "mediaId"),
        @Mapping(source = "menu.replyThumbMediaId", target = "thumbMediaId"),
        @Mapping(source = "menu.replyTitle", target = "title"),
        @Mapping(source = "menu.replyDescription", target = "description"),
        @Mapping(source = "menu.replyArticles", target = "articles"),
        @Mapping(source = "menu.replyMusicUrl", target = "musicUrl"),
        @Mapping(source = "menu.replyHqMusicUrl", target = "hqMusicUrl"),
    })
    MpMessageSendOutReqBO convert(String openid, MpMenuDO menu);

    List<WxMenuButton> convert(List<MpMenuSaveReqVO.Menu> list);

    @Mappings({
        @Mapping(source = "menuKey", target = "key"),
        @Mapping(source = "children", target = "subButtons"),
    })
    WxMenuButton convert(MpMenuSaveReqVO.Menu bean);

    MpMenuDO convert02(MpMenuSaveReqVO.Menu menu);
}
