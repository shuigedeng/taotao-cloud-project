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

package com.taotao.cloud.wechat.biz.weixin.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.joolun.weixin.constant.ConfigConstant;
import com.joolun.weixin.entity.WxAutoReply;
import com.joolun.weixin.entity.WxUser;
import com.joolun.weixin.mapper.WxUserMapper;
import com.joolun.weixin.service.WxAutoReplyService;
import com.joolun.weixin.service.WxMsgService;
import com.joolun.weixin.utils.LocalDateTimeUtils;
import java.util.List;
import java.util.Map;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.stereotype.Component;

/**
 * @author www.joolun.com
 */
@Slf4j
@Component
@AllArgsConstructor
public class SubscribeHandler extends AbstractHandler {
    private final WxAutoReplyService wxAutoReplyService;
    private final WxUserMapper wxUserMapper;
    private final WxMsgService wxMsgService;

    @Override
    public WxMpXmlOutMessage handle(
            WxMpXmlMessage wxMessage,
            Map<String, Object> context,
            WxMpService weixinService,
            WxSessionManager sessionManager) {
        log.info("新关注用户 OPENID: " + wxMessage.getFromUser());
        // 获取微信用户基本信息
        try {
            WxMpUser userWxInfo = weixinService.getUserService().userInfo(wxMessage.getFromUser(), null);
            if (userWxInfo != null) {
                // TODO 添加关注用户到本地数据库
                WxUser wxUser = wxUserMapper.selectOne(
                        Wrappers.<WxUser>lambdaQuery().eq(WxUser::getOpenId, userWxInfo.getOpenId()));
                if (wxUser == null) { // 第一次关注
                    wxUser = new WxUser();
                    wxUser.setSubscribeNum(1);
                    this.setWxUserValue(wxUser, userWxInfo);
                    //						wxUser.setTenantId(wxApp.getTenantId());
                    wxUserMapper.insert(wxUser);
                } else { // 曾经关注过
                    wxUser.setSubscribeNum(wxUser.getSubscribeNum() + 1);
                    this.setWxUserValue(wxUser, userWxInfo);
                    //						wxUser.setTenantId(wxApp.getTenantId());
                    wxUserMapper.updateById(wxUser);
                }
                // 发送关注消息
                List<WxAutoReply> listWxAutoReply = wxAutoReplyService.list(Wrappers.<WxAutoReply>query()
                        .lambda()
                        .eq(WxAutoReply::getType, ConfigConstant.WX_AUTO_REPLY_TYPE_1));
                WxMpXmlOutMessage wxMpXmlOutMessage =
                        MsgHandler.getWxMpXmlOutMessage(wxMessage, listWxAutoReply, wxUser, wxMsgService);
                return wxMpXmlOutMessage;
            }
        } catch (Exception e) {
            log.error("用户关注出错：" + e.getMessage());
        }
        return null;
    }

    public static void setWxUserValue(WxUser wxUser, WxMpUser userWxInfo) {
        wxUser.setAppType(ConfigConstant.WX_APP_TYPE_2);
        ;
        wxUser.setSubscribe(ConfigConstant.SUBSCRIBE_TYPE_YES);
        wxUser.setSubscribeScene(userWxInfo.getSubscribeScene());
        if (null != userWxInfo.getSubscribeTime()) {
            wxUser.setSubscribeTime(LocalDateTimeUtils.timestamToDatetime(userWxInfo.getSubscribeTime() * 1000));
        }
        wxUser.setOpenId(userWxInfo.getOpenId());
        wxUser.setLanguage(userWxInfo.getLanguage());
        wxUser.setRemark(userWxInfo.getRemark());
        wxUser.setUnionId(userWxInfo.getUnionId());
        wxUser.setGroupId(JSONUtil.toJsonStr(userWxInfo.getGroupId()));
        wxUser.setTagidList(userWxInfo.getTagIds());
        wxUser.setQrSceneStr(userWxInfo.getQrSceneStr());
    }
}
