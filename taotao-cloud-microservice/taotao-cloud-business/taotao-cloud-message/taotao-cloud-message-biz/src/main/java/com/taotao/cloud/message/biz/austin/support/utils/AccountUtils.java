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

package com.taotao.cloud.message.biz.austin.support.utils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaRedisBetterConfigImpl;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import com.taotao.cloud.message.biz.austin.common.constant.SendAccountConstant;
import com.taotao.cloud.message.biz.austin.common.dto.account.WeChatMiniProgramAccount;
import com.taotao.cloud.message.biz.austin.common.dto.account.WeChatOfficialAccount;
import com.taotao.cloud.message.biz.austin.common.dto.account.sms.SmsAccount;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.support.dao.ChannelAccountDao;
import com.taotao.cloud.message.biz.austin.support.domain.ChannelAccount;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.redis.RedisTemplateWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 获取账号信息工具类
 *
 * @author 3y
 */
@Slf4j
@Configuration
public class AccountUtils {

    @Autowired private ChannelAccountDao channelAccountDao;
    @Autowired private StringRedisTemplate redisTemplate;

    /** 消息的小程序/微信服务号账号 */
    private ConcurrentMap<ChannelAccount, WxMpService> officialAccountServiceMap =
            new ConcurrentHashMap<>();

    private ConcurrentMap<ChannelAccount, WxMaService> miniProgramServiceMap =
            new ConcurrentHashMap<>();

    @Bean
    public RedisTemplateWxRedisOps redisTemplateWxRedisOps() {
        return new RedisTemplateWxRedisOps(redisTemplate);
    }

    /**
     * 微信小程序：返回 WxMaService 微信服务号：返回 WxMpService 其他渠道：返回XXXAccount账号对象
     *
     * @param sendAccountId
     * @param clazz
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> T getAccountById(Integer sendAccountId, Class<T> clazz) {
        try {
            Optional<ChannelAccount> optionalChannelAccount =
                    channelAccountDao.findById(Long.valueOf(sendAccountId));
            if (optionalChannelAccount.isPresent()) {
                ChannelAccount channelAccount = optionalChannelAccount.get();
                if (clazz.equals(WxMaService.class)) {
                    return (T)
                            ConcurrentHashMapUtils.computeIfAbsent(
                                    miniProgramServiceMap,
                                    channelAccount,
                                    account ->
                                            initMiniProgramService(
                                                    JSON.parseObject(
                                                            account.getAccountConfig(),
                                                            WeChatMiniProgramAccount.class)));
                } else if (clazz.equals(WxMpService.class)) {
                    return (T)
                            ConcurrentHashMapUtils.computeIfAbsent(
                                    officialAccountServiceMap,
                                    channelAccount,
                                    account ->
                                            initOfficialAccountService(
                                                    JSON.parseObject(
                                                            account.getAccountConfig(),
                                                            WeChatOfficialAccount.class)));
                } else {
                    return JSON.parseObject(channelAccount.getAccountConfig(), clazz);
                }
            }
        } catch (Exception e) {
            log.error("AccountUtils#getAccount fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        return null;
    }

    /**
     * 通过脚本名 匹配到对应的短信账号
     *
     * @param scriptName 脚本名
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getSmsAccountByScriptName(String scriptName, Class<T> clazz) {
        try {
            List<ChannelAccount> channelAccountList =
                    channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(
                            CommonConstant.FALSE, ChannelType.SMS.getCode());
            for (ChannelAccount channelAccount : channelAccountList) {
                try {
                    SmsAccount smsAccount =
                            JSON.parseObject(channelAccount.getAccountConfig(), SmsAccount.class);
                    if (smsAccount.getScriptName().equals(scriptName)) {
                        return JSON.parseObject(channelAccount.getAccountConfig(), clazz);
                    }
                } catch (Exception e) {
                    log.error(
                            "AccountUtils#getSmsAccount parse fail! e:{},account:{}",
                            Throwables.getStackTraceAsString(e),
                            JSON.toJSONString(channelAccount));
                }
            }
        } catch (Exception e) {
            log.error("AccountUtils#getSmsAccount fail! e:{}", Throwables.getStackTraceAsString(e));
        }
        log.error("AccountUtils#getSmsAccount not found!:{}", scriptName);
        return null;
    }

    /**
     * 初始化微信服务号 access_token 用redis存储
     *
     * @return
     */
    public WxMpService initOfficialAccountService(WeChatOfficialAccount officialAccount) {
        WxMpService wxMpService = new WxMpServiceImpl();
        WxMpRedisConfigImpl config =
                new WxMpRedisConfigImpl(
                        redisTemplateWxRedisOps(),
                        SendAccountConstant.OFFICIAL_ACCOUNT_ACCESS_TOKEN_PREFIX);
        config.setAppId(officialAccount.getAppId());
        config.setSecret(officialAccount.getSecret());
        config.setToken(officialAccount.getToken());
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }

    /**
     * 初始化微信小程序 access_token 用redis存储
     *
     * @return
     */
    private WxMaService initMiniProgramService(WeChatMiniProgramAccount miniProgramAccount) {
        WxMaService wxMaService = new WxMaServiceImpl();
        WxMaRedisBetterConfigImpl config =
                new WxMaRedisBetterConfigImpl(
                        redisTemplateWxRedisOps(), SendAccountConstant.MINI_PROGRAM_TOKEN_PREFIX);
        config.setAppid(miniProgramAccount.getAppId());
        config.setSecret(miniProgramAccount.getAppSecret());
        wxMaService.setWxMaConfig(config);
        return wxMaService;
    }
}
