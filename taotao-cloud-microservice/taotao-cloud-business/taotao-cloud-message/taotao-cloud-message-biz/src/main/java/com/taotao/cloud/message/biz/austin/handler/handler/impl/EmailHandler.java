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

import org.dromara.hutoolcore.util.StrUtil;
import org.dromara.hutoolextra.mail.MailAccount;
import org.dromara.hutoolextra.mail.MailUtil;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.RateLimiter;
import com.sun.mail.util.MailSSLSocketFactory;
import com.taotao.cloud.message.biz.austin.handler.handler.BaseHandler;
import java.io.File;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 邮件发送处理
 *
 * @author 3y
 */
@Component
@Slf4j
public class EmailHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    @Value("${austin.business.upload.crowd.path}")
    private String dataPath;

    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();

        // 按照请求限流，默认单机 3 qps （具体数值配置在apollo动态调整)
        Double rateInitValue = Double.valueOf(3);
        flowControlParam = FlowControlParam.builder()
                .rateInitValue(rateInitValue)
                .rateLimitStrategy(RateLimitStrategy.REQUEST_RATE_LIMIT)
                .rateLimiter(RateLimiter.create(rateInitValue))
                .build();
    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccountConfig(taskInfo.getSendAccount());
        try {
            File file = StrUtil.isNotBlank(emailContentModel.getUrl())
                    ? AustinFileUtils.getRemoteUrl2File(dataPath, emailContentModel.getUrl())
                    : null;
            String result = Objects.isNull(file)
                    ? MailUtil.send(
                            account,
                            taskInfo.getReceiver(),
                            emailContentModel.getTitle(),
                            emailContentModel.getContent(),
                            true)
                    : MailUtil.send(
                            account,
                            taskInfo.getReceiver(),
                            emailContentModel.getTitle(),
                            emailContentModel.getContent(),
                            true,
                            file);
        } catch (Exception e) {
            log.error("EmailHandler#handler fail!{},params:{}", Throwables.getStackTraceAsString(e), taskInfo);
            return false;
        }
        return true;
    }

    /**
     * 获取账号信息合配置
     *
     * @return
     */
    private MailAccount getAccountConfig(Integer sendAccount) {
        MailAccount account = accountUtils.getAccountById(sendAccount, MailAccount.class);
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setAuth(account.isAuth())
                    .setStarttlsEnable(account.isStarttlsEnable())
                    .setSslEnable(account.isSslEnable())
                    .setCustomProperty("mail.smtp.ssl.socketFactory", sf);
            account.setTimeout(25000).setConnectionTimeout(25000);
        } catch (Exception e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }
        return account;
    }

    @Override
    public void recall(MessageTemplate messageTemplate) {}
}
