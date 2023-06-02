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

package com.taotao.cloud.message.biz.austin.handler.handler.feishu;

import org.dromara.hutoolhttp.ContentType;
import org.dromara.hutoolhttp.Header;
import org.dromara.hutoolhttp.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.constant.SendAccountConstant;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.dto.account.FeiShuRobotAccount;
import com.taotao.cloud.message.biz.austin.common.dto.model.FeiShuRobotContentModel;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.common.enums.SendMessageType;
import com.taotao.cloud.message.biz.austin.handler.domain.feishu.FeiShuRobotParam;
import com.taotao.cloud.message.biz.austin.handler.domain.feishu.FeiShuRobotResult;
import com.taotao.cloud.message.biz.austin.handler.handler.BaseHandler;
import com.taotao.cloud.message.biz.austin.handler.handler.Handler;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import com.taotao.cloud.message.biz.austin.support.utils.AccountUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 企业微信群机器人 消息处理器
 *
 * @author 3y
 */
@Slf4j
@Service
public class FeiShuRobotHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    public FeiShuRobotHandler() {
        channelCode = ChannelType.FEI_SHU_ROBOT.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            FeiShuRobotAccount account = accountUtils.getAccount(
                    taskInfo.getSendAccount(),
                    SendAccountConstant.FEI_SHU_ROBOT_ACCOUNT_KEY,
                    SendAccountConstant.FEI_SHU_ROBOT_PREFIX,
                    FeiShuRobotAccount.class);
            FeiShuRobotParam feiShuRobotParam = assembleParam(taskInfo);
            String result = HttpRequest.post(account.getWebhook())
                    .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .body(JSON.toJSONString(feiShuRobotParam))
                    .timeout(2000)
                    .execute()
                    .body();
            FeiShuRobotResult feiShuRobotResult = JSON.parseObject(result, FeiShuRobotResult.class);
            if (feiShuRobotResult.getStatusCode() == 0) {
                return true;
            }
            log.error(
                    "FeiShuRobotHandler#handler fail! result:{},params:{}",
                    JSON.toJSONString(feiShuRobotResult),
                    JSON.toJSONString(taskInfo));
        } catch (Exception e) {
            log.error(
                    "FeiShuRobotHandler#handler fail!e:{},params:{}",
                    Throwables.getStackTraceAsString(e),
                    JSON.toJSONString(taskInfo));
        }
        return false;
    }

    private FeiShuRobotParam assembleParam(TaskInfo taskInfo) {
        FeiShuRobotContentModel contentModel = (FeiShuRobotContentModel) taskInfo.getContentModel();

        FeiShuRobotParam param = FeiShuRobotParam.builder()
                .msgType(SendMessageType.geFeiShuRobotTypeByCode(contentModel.getSendType()))
                .build();

        if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())) {
            param.setContent(FeiShuRobotParam.ContentDTO.builder()
                    .text(contentModel.getContent())
                    .build());
        }
        if (SendMessageType.RICH_TEXT.getCode().equals(contentModel.getSendType())) {
            List<FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.PostContentDTO> postContentDTOS = JSON.parseArray(
                    contentModel.getPostContent(), FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.PostContentDTO.class);
            List<List<FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.PostContentDTO>> postContentList = new ArrayList<>();
            postContentList.add(postContentDTOS);
            FeiShuRobotParam.ContentDTO.PostDTO postDTO = FeiShuRobotParam.ContentDTO.PostDTO.builder()
                    .zhCn(FeiShuRobotParam.ContentDTO.PostDTO.ZhCnDTO.builder()
                            .title(contentModel.getTitle())
                            .content(postContentList)
                            .build())
                    .build();
            param.setContent(FeiShuRobotParam.ContentDTO.builder().post(postDTO).build());
        }
        if (SendMessageType.SHARE_CHAT.getCode().equals(contentModel.getSendType())) {
            param.setContent(FeiShuRobotParam.ContentDTO.builder()
                    .shareChatId(contentModel.getMediaId())
                    .build());
        }
        if (SendMessageType.IMAGE.getCode().equals(contentModel.getSendType())) {
            param.setContent(FeiShuRobotParam.ContentDTO.builder()
                    .imageKey(contentModel.getMediaId())
                    .build());
        }
        if (SendMessageType.ACTION_CARD.getCode().equals(contentModel.getSendType())) {
            //
        }
        return param;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {}
}
