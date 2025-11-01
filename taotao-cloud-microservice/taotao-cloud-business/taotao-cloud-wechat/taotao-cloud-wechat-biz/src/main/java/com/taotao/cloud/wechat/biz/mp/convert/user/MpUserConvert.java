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

package com.taotao.cloud.wechat.biz.mp.convert.user;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.mp.controller.admin.user.vo.MpUserRespVO;
import cn.iocoder.yudao.module.mp.controller.admin.user.vo.MpUserUpdateReqVO;
import cn.iocoder.yudao.module.mp.dal.dataobject.account.MpAccountDO;
import cn.iocoder.yudao.module.mp.dal.dataobject.user.MpUserDO;
import java.util.List;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MpUserConvert {

    MpUserConvert INSTANCE = Mappers.getMapper(MpUserConvert.class);

    MpUserRespVO convert(MpUserDO bean);

    List<MpUserRespVO> convertList(List<MpUserDO> list);

    PageResult<MpUserRespVO> convertPage(PageResult<MpUserDO> page);

    @Mappings(
            value = {
                @Mapping(source = "openId", target = "openid"),
                @Mapping(source = "headImgUrl", target = "headImageUrl"),
                @Mapping(target = "subscribeTime", ignore = true), // 单独转换
            })
    MpUserDO convert(WxMpUser wxMpUser);

    default MpUserDO convert(MpAccountDO account, WxMpUser wxMpUser) {
        MpUserDO user = convert(wxMpUser);
        user.setSubscribeStatus(
                wxMpUser.getSubscribe() ? CommonStatusEnum.ENABLE.getStatus() : CommonStatusEnum.DISABLE.getStatus());
        user.setSubscribeTime(LocalDateTimeUtil.of(wxMpUser.getSubscribeTime() * 1000L));
        if (account != null) {
            user.setAccountId(account.getId());
            user.setAppId(account.getAppId());
        }
        return user;
    }

    default List<MpUserDO> convertList(MpAccountDO account, List<WxMpUser> wxUsers) {
        return CollectionUtils.convertList(wxUsers, wxUser -> convert(account, wxUser));
    }

    MpUserDO convert(MpUserUpdateReqVO bean);
}
