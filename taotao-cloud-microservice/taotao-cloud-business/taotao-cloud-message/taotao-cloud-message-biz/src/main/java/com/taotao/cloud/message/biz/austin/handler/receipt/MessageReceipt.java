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

import com.google.common.base.Throwables;
import com.taotao.cloud.message.biz.austin.handler.receipt.stater.ReceiptMessageStater;
import com.taotao.cloud.message.biz.austin.support.config.SupportThreadPoolConfig;
import java.util.List;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 拉取回执信息 入口
 *
 * @author 3y
 */
@Component
@Slf4j
public class MessageReceipt {

    @Autowired
    private List<ReceiptMessageStater> receiptMessageStaterList;

    @PostConstruct
    private void init() {
        SupportThreadPoolConfig.getPendingSingleThreadPool().execute(() -> {
            while (true) {
                try {
                    for (ReceiptMessageStater receiptMessageStater : receiptMessageStaterList) {
                        receiptMessageStater.start();
                    }
                    Thread.sleep(2000);
                } catch (Exception e) {
                    log.error("MessageReceipt#init fail:{}", Throwables.getStackTraceAsString(e));
                }
            }
        });
    }
}
