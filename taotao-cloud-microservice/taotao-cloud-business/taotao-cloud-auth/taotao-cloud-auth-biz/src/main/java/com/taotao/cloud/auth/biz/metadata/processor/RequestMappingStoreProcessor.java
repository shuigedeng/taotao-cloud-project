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

package com.taotao.cloud.auth.biz.metadata.processor;

import com.taotao.boot.security.spring.event.domain.RequestMapping;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * <p>RequestMapping存储服务 </p>
 *
 */
@Component
public class RequestMappingStoreProcessor {

    private static final Logger log = LoggerFactory.getLogger(RequestMappingStoreProcessor.class);

    private final SecurityMetadataDistributeProcessor securityMetadataDistributeProcessor;

    @Autowired
    public RequestMappingStoreProcessor(
            SecurityMetadataDistributeProcessor securityMetadataDistributeProcessor) {
        this.securityMetadataDistributeProcessor = securityMetadataDistributeProcessor;
    }

    @Async
    public void postProcess(List<RequestMapping> requestMappings) {
        log.debug(" [4] Async store request mapping process BEGIN!");
        securityMetadataDistributeProcessor.postRequestMappings(requestMappings);
    }
}
