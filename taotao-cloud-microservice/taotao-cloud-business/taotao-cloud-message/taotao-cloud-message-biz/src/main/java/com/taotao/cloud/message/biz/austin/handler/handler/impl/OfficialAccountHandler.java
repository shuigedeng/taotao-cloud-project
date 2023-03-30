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

package com.taotao.cloud.message.biz.austin.handler.handler.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author zyg 微信服务号推送处理
 */
@Component
@Slf4j
public class OfficialAccountHandler extends BaseHandler implements Handler {

    @Autowired private AccountUtils accountUtils;

    public OfficialAccountHandler() {
        channelCode = ChannelType.OFFICIAL_ACCOUNT.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            OfficialAccountsContentModel contentModel =
                    (OfficialAccountsContentModel) taskInfo.getContentModel();
            WxMpService wxMpService =
                    accountUtils.getAccountById(taskInfo.getSendAccount(), WxMpService.class);
            List<WxMpTemplateMessage> messages = assembleReq(taskInfo.getReceiver(), contentModel);
            for (WxMpTemplateMessage message : messages) {
                try {
                    wxMpService.getTemplateMsgService().sendTemplateMsg(message);
                } catch (Exception e) {
                    log.info(
                            "OfficialAccountHandler#handler fail! param:{},e:{}",
                            JSON.toJSONString(taskInfo),
                            Throwables.getStackTraceAsString(e));
                }
            }
            return true;
        } catch (Exception e) {
            log.error(
                    "OfficialAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e),
                    JSON.toJSONString(taskInfo));
        }
        return false;
    }

    /** 组装发送模板信息参数 */
    private List<WxMpTemplateMessage> assembleReq(
            Set<String> receiver, OfficialAccountsContentModel contentModel) {
        List<WxMpTemplateMessage> wxMpTemplateMessages = new ArrayList<>(receiver.size());
        for (String openId : receiver) {
            WxMpTemplateMessage templateMessage =
                    WxMpTemplateMessage.builder()
                            .toUser(openId)
                            .templateId(contentModel.getTemplateId())
                            .url(contentModel.getUrl())
                            .data(getWxMpTemplateData(contentModel.getOfficialAccountParam()))
                            .miniProgram(
                                    new WxMpTemplateMessage.MiniProgram(
                                            contentModel.getMiniProgramId(),
                                            contentModel.getPath(),
                                            false))
                            .build();
            wxMpTemplateMessages.add(templateMessage);
        }
        return wxMpTemplateMessages;
    }

    /**
     * 构建模板消息参数
     *
     * @return
     */
    private List<WxMpTemplateData> getWxMpTemplateData(Map<String, String> data) {
        List<WxMpTemplateData> templateDataList = new ArrayList<>(data.size());
        data.forEach((k, v) -> templateDataList.add(new WxMpTemplateData(k, v)));
        return templateDataList;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {}
}
