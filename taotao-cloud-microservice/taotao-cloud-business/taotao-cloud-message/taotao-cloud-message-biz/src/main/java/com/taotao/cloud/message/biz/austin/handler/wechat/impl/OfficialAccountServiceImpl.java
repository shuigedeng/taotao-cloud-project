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

package com.taotao.cloud.message.biz.austin.handler.wechat.impl;

import com.taotao.cloud.message.biz.austin.common.constant.SendAccountConstant;
import com.taotao.cloud.message.biz.austin.common.dto.account.WeChatOfficialAccount;
import com.taotao.cloud.message.biz.austin.handler.domain.wechat.WeChatOfficialParam;
import com.taotao.cloud.message.biz.austin.handler.wechat.OfficialAccountService;
import com.taotao.cloud.message.biz.austin.support.utils.AccountUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zyg
 */
@Service
@Slf4j
public class OfficialAccountServiceImpl implements OfficialAccountService {

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public List<String> send(WeChatOfficialParam officialParam) throws Exception {
        WeChatOfficialAccount officialAccount = accountUtils.getAccount(
                officialParam.getSendAccount(),
                SendAccountConstant.WECHAT_OFFICIAL_ACCOUNT_KEY,
                SendAccountConstant.WECHAT_OFFICIAL__PREFIX,
                WeChatOfficialAccount.class);
        WxMpService wxMpService = initService(officialAccount);
        List<WxMpTemplateMessage> messages = assembleReq(officialParam, officialAccount);
        List<String> messageIds = new ArrayList<>(messages.size());
        for (WxMpTemplateMessage wxMpTemplateMessage : messages) {
            String msgId = wxMpService.getTemplateMsgService().sendTemplateMsg(wxMpTemplateMessage);
            messageIds.add(msgId);
        }
        return messageIds;
    }

    /** 组装发送模板信息参数 */
    private List<WxMpTemplateMessage> assembleReq(
            WeChatOfficialParam officialParam, WeChatOfficialAccount officialAccount) {
        Set<String> receiver = officialParam.getOpenIds();
        List<WxMpTemplateMessage> wxMpTemplateMessages = new ArrayList<>(receiver.size());

        // 构建微信模板消息
        for (String openId : receiver) {
            WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                    .toUser(openId)
                    .templateId(officialAccount.getTemplateId())
                    .url(officialAccount.getUrl())
                    .data(getWxMpTemplateData(officialParam.getData()))
                    .miniProgram(new WxMpTemplateMessage.MiniProgram(
                            officialAccount.getMiniProgramId(), officialAccount.getPath(), false))
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

    /**
     * 初始化微信服务号
     *
     * @return
     */
    public WxMpService initService(WeChatOfficialAccount officialAccount) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId(officialAccount.getAppId());
        config.setSecret(officialAccount.getSecret());
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }
}
