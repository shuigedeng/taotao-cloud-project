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

package com.taotao.cloud.message.biz.austin.handler.handler.ali;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.dto.model.AlipayMiniProgramContentModel;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.handler.alipay.AlipayMiniProgramAccountService;
import com.taotao.cloud.message.biz.austin.handler.domain.alipay.AlipayMiniProgramParam;
import com.taotao.cloud.message.biz.austin.handler.handler.BaseHandler;
import com.taotao.cloud.message.biz.austin.handler.handler.Handler;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author jwq 支付宝小程序发送订阅消息
 */
@Component
@Slf4j
public class AlipayMiniProgramAccountHandler extends BaseHandler implements Handler {

    @Autowired
    private AlipayMiniProgramAccountService alipayMiniProgramAccountService;

    public AlipayMiniProgramAccountHandler() {
        channelCode = ChannelType.ALIPAY_MINI_PROGRAM.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        AlipayMiniProgramParam miniProgramParam = buildMiniProgramParam(taskInfo);
        try {
            alipayMiniProgramAccountService.send(miniProgramParam);
        } catch (Exception e) {
            log.error(
                    "AlipayMiniProgramAccountHandler#handler fail:{},params:{}",
                    Throwables.getStackTraceAsString(e),
                    JSON.toJSONString(taskInfo));
            return false;
        }
        return true;
    }

    /**
     * 通过taskInfo构建小程序订阅消息
     *
     * @param taskInfo 任务信息
     * @return AlipayMiniProgramParam
     */
    private AlipayMiniProgramParam buildMiniProgramParam(TaskInfo taskInfo) {
        AlipayMiniProgramParam param = AlipayMiniProgramParam.builder()
                .toUserId(taskInfo.getReceiver())
                .messageTemplateId(taskInfo.getMessageTemplateId())
                .sendAccount(taskInfo.getSendAccount())
                .build();

        AlipayMiniProgramContentModel contentModel = (AlipayMiniProgramContentModel) taskInfo.getContentModel();
        param.setData(contentModel.getMap());
        return param;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {}
}
