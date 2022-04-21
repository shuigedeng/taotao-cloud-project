package com.taotao.cloud.message.biz.austin.handler.handler.impl;


import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.RateLimiter;
import com.sun.mail.util.MailSSLSocketFactory;
import com.taotao.cloud.message.biz.austin.common.constant.SendAccountConstant;
import com.taotao.cloud.message.biz.austin.common.domain.TaskInfo;
import com.taotao.cloud.message.biz.austin.common.dto.model.EmailContentModel;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.handler.enums.RateLimitStrategy;
import com.taotao.cloud.message.biz.austin.handler.flowcontrol.FlowControlParam;
import com.taotao.cloud.message.biz.austin.handler.handler.BaseHandler;
import com.taotao.cloud.message.biz.austin.handler.handler.Handler;
import com.taotao.cloud.message.biz.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 邮件发送处理
 *
 *
 */
@Component
@Slf4j
public class EmailHandler extends BaseHandler implements Handler {

    @Autowired
    private AccountUtils accountUtils;

    public EmailHandler() {
        channelCode = ChannelType.EMAIL.getCode();

        // 按照请求限流，默认单机 3 qps （具体数值配置在apollo动态调整)
        Double rateInitValue = Double.valueOf(3);
        flowControlParam = FlowControlParam.builder().rateInitValue(rateInitValue)
                .rateLimitStrategy(RateLimitStrategy.REQUEST_RATE_LIMIT)
                .rateLimiter(RateLimiter.create(rateInitValue)).build();

    }

    @Override
    public boolean handler(TaskInfo taskInfo) {
        EmailContentModel emailContentModel = (EmailContentModel) taskInfo.getContentModel();
        MailAccount account = getAccountConfig(taskInfo.getSendAccount());
        try {
            MailUtil.send(account, taskInfo.getReceiver(), emailContentModel.getTitle(),
                    emailContentModel.getContent(), true, null);
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
        MailAccount account = accountUtils.getAccount(sendAccount, SendAccountConstant.EMAIL_ACCOUNT_KEY, SendAccountConstant.EMAIL_ACCOUNT_PREFIX, new MailAccount());
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            account.setAuth(true).setStarttlsEnable(true).setSslEnable(true).setCustomProperty("mail.smtp.ssl.socketFactory", sf);
            account.setTimeout(25000).setConnectionTimeout(25000);
        } catch (Exception e) {
            log.error("EmailHandler#getAccount fail!{}", Throwables.getStackTraceAsString(e));
        }
        return account;
    }

}
