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

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.WxMaSubscribeService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.api.impl.WxMaSubscribeServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.taotao.cloud.message.biz.austin.common.constant.SendAccountConstant;
import com.taotao.cloud.message.biz.austin.common.dto.account.WeChatMiniProgramAccount;
import com.taotao.cloud.message.biz.austin.handler.domain.wechat.WeChatMiniProgramParam;
import com.taotao.cloud.message.biz.austin.handler.wechat.MiniProgramAccountService;
import com.taotao.cloud.message.biz.austin.support.utils.AccountUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author sunql
 * @since 2022年05月06日 16:41
 */
@Service
@Slf4j
public class MiniProgramAccountServiceImpl implements MiniProgramAccountService {

    @Autowired
    private AccountUtils accountUtils;

    @Override
    public void send(WeChatMiniProgramParam miniProgramParam) throws Exception {
        WeChatMiniProgramAccount miniProgramAccount = accountUtils.getAccount(
                miniProgramParam.getSendAccount(),
                SendAccountConstant.WECHAT_MINI_PROGRAM_ACCOUNT_KEY,
                SendAccountConstant.WECHAT_MINI_PROGRAM_PREFIX,
                WeChatMiniProgramAccount.class);

        WxMaSubscribeService wxMaSubscribeService = initService(miniProgramAccount);
        List<WxMaSubscribeMessage> subscribeMessageList = assembleReq(miniProgramParam, miniProgramAccount);
        for (WxMaSubscribeMessage subscribeMessage : subscribeMessageList) {
            wxMaSubscribeService.sendSubscribeMsg(subscribeMessage);
        }
    }

    /** 组装发送模板信息参数 */
    private List<WxMaSubscribeMessage> assembleReq(
            WeChatMiniProgramParam miniProgramParam, WeChatMiniProgramAccount miniProgramAccount) {
        Set<String> receiver = miniProgramParam.getOpenIds();
        List<WxMaSubscribeMessage> messageList = new ArrayList<>(receiver.size());

        // 构建微信小程序订阅消息
        for (String openId : receiver) {
            WxMaSubscribeMessage subscribeMessage = WxMaSubscribeMessage.builder()
                    .toUser(openId)
                    .data(getWxMTemplateData(miniProgramParam.getData()))
                    .miniprogramState(miniProgramAccount.getMiniProgramState())
                    .templateId(miniProgramAccount.getTemplateId())
                    .page(miniProgramAccount.getPage())
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
    private List<WxMaSubscribeMessage.MsgData> getWxMTemplateData(Map<String, String> data) {
        List<WxMaSubscribeMessage.MsgData> templateDataList = new ArrayList<>(data.size());
        data.forEach((k, v) -> templateDataList.add(new WxMaSubscribeMessage.MsgData(k, v)));
        return templateDataList;
    }

    /**
     * 初始化微信小程序
     *
     * @return
     */
    private WxMaSubscribeServiceImpl initService(WeChatMiniProgramAccount miniProgramAccount) {
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaDefaultConfigImpl wxMaConfig = new WxMaDefaultConfigImpl();
        wxMaConfig.setAppid(miniProgramAccount.getAppId());
        wxMaConfig.setSecret(miniProgramAccount.getAppSecret());
        wxMaService.setWxMaConfig(wxMaConfig);
        return new WxMaSubscribeServiceImpl(wxMaService);
    }
}
