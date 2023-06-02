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

package com.taotao.cloud.message.biz.austin.cron.handler;

import org.dromara.hutoolcore.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiGettokenRequest;
import com.dingtalk.api.response.OapiGettokenResponse;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import com.taotao.cloud.message.biz.austin.common.constant.SendAccountConstant;
import com.taotao.cloud.message.biz.austin.common.dto.account.DingDingWorkNoticeAccount;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.support.config.SupportThreadPoolConfig;
import com.taotao.cloud.message.biz.austin.support.dao.ChannelAccountDao;
import com.taotao.cloud.message.biz.austin.support.domain.ChannelAccount;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 刷新钉钉的access_token
 *
 * <p>https://open.dingtalk.com/document/orgapp-server/obtain-orgapp-token
 *
 * @author 3y
 */
@Service
@Slf4j
public class RefreshDingDingAccessTokenHandler {

    private static final String URL = "https://oapi.dingtalk.com/gettoken";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ChannelAccountDao channelAccountDao;

    /** 每小时请求一次接口刷新（以防失效) */
    @XxlJob("refreshAccessTokenJob")
    public void execute() {
        log.info("refreshAccessTokenJob#execute!");
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            List<ChannelAccount> accountList = channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(
                    CommonConstant.FALSE, ChannelType.DING_DING_WORK_NOTICE.getCode());
            for (ChannelAccount channelAccount : accountList) {
                DingDingWorkNoticeAccount account =
                        JSON.parseObject(channelAccount.getAccountConfig(), DingDingWorkNoticeAccount.class);
                String accessToken = getAccessToken(account);
                if (StrUtil.isNotBlank(accessToken)) {
                    redisTemplate
                            .opsForValue()
                            .set(
                                    SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX + channelAccount.getId(),
                                    accessToken);
                }
            }
        });
    }

    /**
     * 获取 access_token
     *
     * @param account
     * @return
     */
    private String getAccessToken(DingDingWorkNoticeAccount account) {
        String accessToken = "";
        try {
            DingTalkClient client = new DefaultDingTalkClient(URL);
            OapiGettokenRequest req = new OapiGettokenRequest();
            req.setAppkey(account.getAppKey());
            req.setAppsecret(account.getAppSecret());
            req.setHttpMethod(CommonConstant.REQUEST_METHOD_GET);
            OapiGettokenResponse rsp = client.execute(req);
            accessToken = rsp.getAccessToken();
        } catch (Exception e) {
            log.error("RefreshDingDingAccessTokenHandler#getAccessToken fail:{}", Throwables.getStackTraceAsString(e));
        }
        return accessToken;
    }
}
