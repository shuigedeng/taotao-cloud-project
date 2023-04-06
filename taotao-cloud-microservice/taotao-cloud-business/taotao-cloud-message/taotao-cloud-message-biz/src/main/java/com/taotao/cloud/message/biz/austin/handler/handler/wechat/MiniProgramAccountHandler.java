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

package com.taotao.cloud.message.biz.austin.handler.handler.wechat;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.dto.model.MiniProgramContentModel;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.handler.domain.wechat.WeChatMiniProgramParam;
import com.taotao.cloud.message.biz.austin.handler.handler.BaseHandler;
import com.taotao.cloud.message.biz.austin.handler.handler.Handler;
import com.taotao.cloud.message.biz.austin.handler.wechat.MiniProgramAccountService;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
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
    private MiniProgramAccountService miniProgramAccountService;

    public MiniProgramAccountHandler() {
        channelCode = ChannelType.MINI_PROGRAM.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        WeChatMiniProgramParam miniProgramParam = buildMiniProgramParam(taskInfo);
        try {
            miniProgramAccountService.send(miniProgramParam);
        } catch (Exception e) {
            log.error(
                    "MiniProgramAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e),
                    JSON.toJSONString(taskInfo));
            return false;
        }
        return true;
    }

    /**
     * 通过taskInfo构建小程序订阅消息
     *
     * @param taskInfo
     * @return
     */
    private WeChatMiniProgramParam buildMiniProgramParam(TaskInfo taskInfo) {
        // 小程序订阅消息可以关联到系统业务，通过接口查询。
        WeChatMiniProgramParam miniProgramParam = WeChatMiniProgramParam.builder()
                .openIds(taskInfo.getReceiver())
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccount(taskInfo.getSendAccount())
                .build();

        MiniProgramContentModel contentModel = (MiniProgramContentModel) taskInfo.getContentModel();
        miniProgramParam.setData(contentModel.getMap());
        return miniProgramParam;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {}
}
