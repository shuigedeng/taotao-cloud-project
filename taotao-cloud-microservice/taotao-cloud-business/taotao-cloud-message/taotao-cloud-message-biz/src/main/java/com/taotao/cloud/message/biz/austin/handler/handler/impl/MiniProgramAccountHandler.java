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

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author sunql 微信小程序发送订阅消息
 */
@Component
@Slf4j
public class MiniProgramAccountHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    public MiniProgramAccountHandler() {
        channelCode = ChannelType.MINI_PROGRAM.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        MiniProgramContentModel contentModel = (MiniProgramContentModel) taskInfo.getContentModel();
        WxMaService wxMaService = accountUtils.getAccountById(taskInfo.getSendAccount(), WxMaService.class);
        List<WxMaSubscribeMessage> wxMaSubscribeMessages = assembleReq(taskInfo.getReceiver(), contentModel);
        for (WxMaSubscribeMessage message : wxMaSubscribeMessages) {
            try {
                wxMaService.getSubscribeService().sendSubscribeMsg(message);
            } catch (Exception e) {
                log.info(
                        "MiniProgramAccountHandler#handler fail! param:{},e:{}",
                        JSON.toJSONString(taskInfo),
                        Throwables.getStackTraceAsString(e));
            }
        }
        return true;
    }

    /** 组装发送模板信息参数 */
    private List<WxMaSubscribeMessage> assembleReq(Set<String> receiver, MiniProgramContentModel contentModel) {
        List<WxMaSubscribeMessage> messageList = new ArrayList<>(receiver.size());
        for (String openId : receiver) {
            WxMaSubscribeMessage subscribeMessage = WxMaSubscribeMessage.builder()
                    .toUser(openId)
                    .data(getWxMaTemplateData(contentModel.getMiniProgramParam()))
                    .templateId(contentModel.getTemplateId())
                    .page(contentModel.getPage())
                    .build();
            messageList.add(subscribeMessage);
        }
        return messageList;
    }

    /**
     * 构建订阅消息参数
     *
     * @returnp
     */
    private List<WxMaSubscribeMessage.MsgData> getWxMaTemplateData(Map<String, String> data) {
        List<WxMaSubscribeMessage.MsgData> templateDataList = new ArrayList<>(data.size());
        data.forEach((k, v) -> templateDataList.add(new WxMaSubscribeMessage.MsgData(k, v)));
        return templateDataList;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {}
}
