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

import org.dromara.hutoolcore.codec.Base64;
import org.dromara.hutoolcore.io.file.FileReader;
import org.dromara.hutoolcrypto.digest.DigestUtil;
import org.dromara.hutoolhttp.ContentType;
import org.dromara.hutoolhttp.Header;
import org.dromara.hutoolhttp.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.constant.SendAccountConstant;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.dto.account.EnterpriseWeChatRobotAccount;
import com.taotao.cloud.message.biz.austin.common.dto.model.EnterpriseWeChatRobotContentModel;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.common.enums.SendMessageType;
import com.taotao.cloud.message.biz.austin.handler.domain.wechat.robot.EnterpriseWeChatRobotParam;
import com.taotao.cloud.message.biz.austin.handler.handler.BaseHandler;
import com.taotao.cloud.message.biz.austin.handler.handler.Handler;
import com.taotao.cloud.message.biz.austin.support.domain.MessageTemplate;
import com.taotao.cloud.message.biz.austin.support.utils.AccountUtils;
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
public class EnterpriseWeChatRobotHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    public EnterpriseWeChatRobotHandler() {
        channelCode = ChannelType.ENTERPRISE_WE_CHAT_ROBOT.getCode();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        try {
            EnterpriseWeChatRobotAccount account = accountUtils.getAccount(
                    taskInfo.getSendAccount(),
                    SendAccountConstant.ENTERPRISE_WECHAT_ROBOT_ACCOUNT_KEY,
                    SendAccountConstant.ENTERPRISE_WECHAT_ROBOT_PREFIX,
                    EnterpriseWeChatRobotAccount.class);
            EnterpriseWeChatRobotParam enterpriseWeChatRobotParam = assembleParam(taskInfo);
            String result = HttpRequest.post(account.getWebhook())
                    .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .body(JSON.toJSONString(enterpriseWeChatRobotParam))
                    .timeout(2000)
                    .execute()
                    .body();
            JSONObject jsonObject = JSON.parseObject(result);
            if (jsonObject.getInteger("errcode") != 0) {
                return true;
            }
            log.error(
                    "EnterpriseWeChatRobotHandler#handler fail! result:{},params:{}",
                    JSON.toJSONString(jsonObject),
                    JSON.toJSONString(taskInfo));
        } catch (Exception e) {
            log.error(
                    "EnterpriseWeChatRobotHandler#handler fail!e:{},params:{}",
                    Throwables.getStackTraceAsString(e),
                    JSON.toJSONString(taskInfo));
        }
        return false;
    }

    private EnterpriseWeChatRobotParam assembleParam(TaskInfo taskInfo) {
        EnterpriseWeChatRobotContentModel contentModel = (EnterpriseWeChatRobotContentModel) taskInfo.getContentModel();
        EnterpriseWeChatRobotParam param = EnterpriseWeChatRobotParam.builder()
                .msgType(SendMessageType.getEnterpriseWeChatRobotTypeByCode(contentModel.getSendType()))
                .build();

        if (SendMessageType.TEXT.getCode().equals(contentModel.getSendType())) {
            param.setText(EnterpriseWeChatRobotParam.TextDTO.builder()
                    .content(contentModel.getContent())
                    .build());
        }
        if (SendMessageType.MARKDOWN.getCode().equals(contentModel.getSendType())) {
            param.setMarkdown(EnterpriseWeChatRobotParam.MarkdownDTO.builder()
                    .content(contentModel.getContent())
                    .build());
        }
        if (SendMessageType.IMAGE.getCode().equals(contentModel.getSendType())) {
            FileReader fileReader = new FileReader(contentModel.getImagePath());
            byte[] bytes = fileReader.readBytes();
            param.setImage(EnterpriseWeChatRobotParam.ImageDTO.builder()
                    .base64(Base64.encode(bytes))
                    .md5(DigestUtil.md5Hex(bytes))
                    .build());
        }
        if (SendMessageType.FILE.getCode().equals(contentModel.getSendType())) {
            param.setFile(EnterpriseWeChatRobotParam.FileDTO.builder()
                    .mediaId(contentModel.getMediaId())
                    .build());
        }
        if (SendMessageType.NEWS.getCode().equals(contentModel.getSendType())) {
            List<EnterpriseWeChatRobotParam.NewsDTO.ArticlesDTO> articlesDTOS =
                    JSON.parseArray(contentModel.getArticles(), EnterpriseWeChatRobotParam.NewsDTO.ArticlesDTO.class);
            param.setNews(EnterpriseWeChatRobotParam.NewsDTO.builder()
                    .articles(articlesDTOS)
                    .build());
        }
        if (SendMessageType.TEMPLATE_CARD.getCode().equals(contentModel.getSendType())) {
            //
        }
        return param;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {}
}
