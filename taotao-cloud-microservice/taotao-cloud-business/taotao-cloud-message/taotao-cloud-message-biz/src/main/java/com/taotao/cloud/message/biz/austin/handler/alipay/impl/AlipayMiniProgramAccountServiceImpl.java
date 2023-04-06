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

package com.taotao.cloud.message.biz.austin.handler.alipay.impl;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.domain.AlipayOpenAppMiniTemplatemessageSendModel;
import com.alipay.api.request.AlipayOpenAppMiniTemplatemessageSendRequest;
import com.taotao.cloud.message.biz.austin.common.dto.account.AlipayMiniProgramAccount;
import com.taotao.cloud.message.biz.austin.handler.alipay.AlipayMiniProgramAccountService;
import com.taotao.cloud.message.biz.austin.handler.config.AlipayClientSingleton;
import com.taotao.cloud.message.biz.austin.handler.domain.alipay.AlipayMiniProgramParam;
import com.taotao.cloud.message.biz.austin.support.utils.AccountUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author jwq 支付宝小程序发送订阅消息实现
 */
@Service
@Slf4j
public class AlipayMiniProgramAccountServiceImpl implements AlipayMiniProgramAccountService {

    @Autowired
    private AccountUtils accountUtils;

    /**
     * 发送订阅消息
     *
     * @param miniProgramParam 订阅消息参数
     * @throws AlipayApiException alipay异常
     */
    @Override
    public void send(AlipayMiniProgramParam miniProgramParam) throws AlipayApiException {
        AlipayMiniProgramAccount miniProgramAccount =
                accountUtils.getAccountById(miniProgramParam.getSendAccount(), AlipayMiniProgramAccount.class);

        AlipayClient client = AlipayClientSingleton.getSingleton(miniProgramAccount);
        List<AlipayOpenAppMiniTemplatemessageSendRequest> request = assembleReq(miniProgramParam, miniProgramAccount);
        for (AlipayOpenAppMiniTemplatemessageSendRequest req : request) {
            client.execute(req);
        }
    }

    /** 组装模板消息的参数 */
    private List<AlipayOpenAppMiniTemplatemessageSendRequest> assembleReq(
            AlipayMiniProgramParam alipayMiniProgramParam, AlipayMiniProgramAccount alipayMiniProgramAccount) {
        Set<String> receiver = alipayMiniProgramParam.getToUserId();
        List<AlipayOpenAppMiniTemplatemessageSendRequest> requestList = new ArrayList<>(receiver.size());

        for (String toUserId : receiver) {
            AlipayOpenAppMiniTemplatemessageSendRequest request = new AlipayOpenAppMiniTemplatemessageSendRequest();
            AlipayOpenAppMiniTemplatemessageSendModel model = new AlipayOpenAppMiniTemplatemessageSendModel();
            model.setToUserId(toUserId);
            model.setUserTemplateId(alipayMiniProgramAccount.getUserTemplateId());
            model.setPage(alipayMiniProgramAccount.getPage());
            model.setData(alipayMiniProgramParam.getData().toString());
            request.setBizModel(model);
            requestList.add(request);
        }
        return requestList;
    }

    //    /**
    //     * 初始化支付宝小程序
    //     */
    //    private AlipayClient initService(AlipayMiniProgramAccount alipayMiniProgramAccount) throws
    // AlipayApiException {
    //        AlipayConfig alipayConfig = new AlipayConfig();
    //        alipayConfig.setServerUrl("https://openapi.alipaydev.com/gateway.do");
    //        alipayConfig.setAppId(alipayMiniProgramAccount.getAppId());
    //        alipayConfig.setPrivateKey(alipayMiniProgramAccount.getPrivateKey());
    //        alipayConfig.setFormat("json");
    //        alipayConfig.setAlipayPublicKey(alipayMiniProgramAccount.getAlipayPublicKey());
    //        alipayConfig.setCharset("utf-8");
    //        alipayConfig.setSignType("RSA2");
    //        return new DefaultAlipayClient(alipayConfig);
    //    }

}
