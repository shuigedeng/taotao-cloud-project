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

import com.taotao.cloud.message.biz.austin.handler.handler.impl.DingDingWorkNoticeHandler;
import com.taotao.cloud.message.biz.austin.handler.receipt.stater.ReceiptMessageStater;
import com.taotao.cloud.message.biz.austin.support.dao.ChannelAccountDao;
import com.taotao.cloud.message.biz.austin.support.domain.ChannelAccount;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 拉取 钉钉工作消息的回执 内容 【未完成】
 *
 * @author 3y
 */
public class DingDingWorkReceiptStaterImpl implements ReceiptMessageStater {

    @Autowired private DingDingWorkNoticeHandler workNoticeHandler;

    @Autowired private ChannelAccountDao channelAccountDao;

    @Override
    public void start() {
        List<ChannelAccount> accountList =
                channelAccountDao.findAllByIsDeletedEqualsAndSendChannelEquals(
                        CommonConstant.FALSE, ChannelType.DING_DING_WORK_NOTICE.getCode());
        for (ChannelAccount channelAccount : accountList) {
            workNoticeHandler.pull(channelAccount.getId());
        }
    }
}
