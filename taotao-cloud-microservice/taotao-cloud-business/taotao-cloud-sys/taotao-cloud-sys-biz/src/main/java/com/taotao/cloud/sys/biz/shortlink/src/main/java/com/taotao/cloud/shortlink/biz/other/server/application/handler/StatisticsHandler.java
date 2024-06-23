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

package com.taotao.cloud.sys.biz.shortlink.src.main.java.com.taotao.cloud.shortlink.biz.other.server.application.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shorturl.server.server.application.dto.UrlRequest;
import shorturl.server.server.application.dto.UrlResponse;
import shorturl.server.server.application.threadpool.CommonExecutor;

@Slf4j
@Component
public class StatisticsHandler implements Handler {

    @Autowired
    CommonExecutor commonExecutor;

    @Override
    public void handle(UrlRequest urlReq, UrlResponse urlResponse) {
        //
        commonExecutor.runTask(() -> {
            log.info("requestId {}async StaticsHandler", urlReq.getRequestId());
            /*to Statistic */
        });
    }
}
