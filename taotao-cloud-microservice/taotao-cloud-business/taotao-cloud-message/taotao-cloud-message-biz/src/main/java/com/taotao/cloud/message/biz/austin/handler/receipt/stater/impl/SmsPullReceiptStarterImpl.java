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

package com.taotao.cloud.message.biz.austin.handler.receipt.stater.impl;

import org.dromara.hutoolcore.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.common.constant.CommonConstant;
import com.taotao.cloud.message.biz.austin.common.dto.account.sms.SmsAccount;
import com.taotao.cloud.message.biz.austin.common.enums.ChannelType;
import com.taotao.cloud.message.biz.austin.handler.receipt.stater.ReceiptMessageStater;
import com.taotao.cloud.message.biz.austin.handler.script.SmsScript;
import com.taotao.cloud.message.biz.austin.support.dao.ChannelAccountDao;
import com.taotao.cloud.message.biz.austin.support.dao.SmsRecordDao;
import com.taotao.cloud.message.biz.austin.support.domain.ChannelAccount;
import com.taotao.cloud.message.biz.austin.support.domain.SmsRecord;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 拉取短信回执信息
 *
 * @author 3y
 */
@Component
@Slf4j
public class SmsPullReceiptStarterImpl implements ReceiptMessageStater {

    @Autowired
    private ChannelAccountDao channelAccountDao;

    @Autowired
    private Map<String, SmsScript> scriptMap;

    @Autowired
    private SmsRecordDao smsRecordDao;

    /** 拉取消息并入库 */
    @Override
    public void start() {
        try {
            List<ChannelAccount> channelAccountList = channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(
                    CommonConstant.FALSE, ChannelType.SMS.getCode());
            for (ChannelAccount channelAccount : channelAccountList) {
                SmsAccount smsAccount = JSON.parseObject(channelAccount.getAccountConfig(), SmsAccount.class);
                List<SmsRecord> smsRecordList = scriptMap
                        .get(smsAccount.getScriptName())
                        .pull(channelAccount.getId().intValue());
                if (CollUtil.isNotEmpty(smsRecordList)) {
                    smsRecordDao.saveAll(smsRecordList);
                }
            }
        } catch (Exception e) {
            log.error("SmsPullReceiptStarter#start fail:{}", Throwables.getStackTraceAsString(e));
        }
    }
}
