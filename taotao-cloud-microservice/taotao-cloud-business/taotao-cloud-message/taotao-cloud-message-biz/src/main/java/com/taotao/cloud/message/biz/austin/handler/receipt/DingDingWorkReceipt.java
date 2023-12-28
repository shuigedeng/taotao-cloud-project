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

package com.taotao.cloud.message.biz.austin.handler.receipt;

import com.taotao.cloud.message.biz.austin.support.utils.AccountUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 拉取钉钉工作消息回执信息
 *
 * @author 3y
 */
@Component
@Slf4j
public class DingDingWorkReceipt {

    private static final String URL = "https://oapi.dingtalk.com/topapi/message/corpconversation/getsendresult";

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AccountUtils accountUtils;

    public void pull() {
        //        try {
        //            for (int index = SendAccountConstant.START; true; index = index +
        // SendAccountConstant.STEP) {
        //                DingDingWorkNoticeAccount account = accountUtils.getAccount(index,
        // SendAccountConstant.DING_DING_WORK_NOTICE_ACCOUNT_KEY,
        // SendAccountConstant.DING_DING_WORK_NOTICE_PREFIX, DingDingWorkNoticeAccount.class);
        //                if (account == null) {
        //                    break;
        //                }
        //                String accessToken =
        // redisTemplate.opsForValue().get(SendAccountConstant.DING_DING_ACCESS_TOKEN_PREFIX +
        // index);
        //                DingTalkClient client = new DefaultDingTalkClient(URL);
        //                OapiMessageCorpconversationGetsendresultRequest req = new
        // OapiMessageCorpconversationGetsendresultRequest();
        //                req.setAgentId(Long.valueOf(account.getAgentId()));
        //                req.setTaskId(456L);
        //                OapiMessageCorpconversationGetsendresultResponse rsp = client.execute(req,
        // accessToken);
        //                LogUtils.info(rsp.getBody());
        //            }
        //        } catch (Exception e) {
        //            log.error("DingDingWorkReceipt#pull");
        //        }
    }
}
