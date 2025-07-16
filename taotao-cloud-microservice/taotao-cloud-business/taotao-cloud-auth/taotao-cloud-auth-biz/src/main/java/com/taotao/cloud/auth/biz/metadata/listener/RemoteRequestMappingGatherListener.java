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

package com.taotao.cloud.auth.biz.metadata.listener;

import com.taotao.boot.common.utils.common.JsonUtils;
import com.taotao.boot.security.spring.event.RemoteRequestMappingGatherEvent;
import com.taotao.boot.security.spring.event.domain.RequestMapping;
import com.taotao.cloud.auth.biz.metadata.processor.RequestMappingStoreProcessor;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * <p>SecurityMetadata远程变更事件监听 </p>
 *
 */
@Component
public class RemoteRequestMappingGatherListener
        implements ApplicationListener<RemoteRequestMappingGatherEvent> {

    private static final Logger log =
            LoggerFactory.getLogger(RemoteRequestMappingGatherListener.class);

    private final RequestMappingStoreProcessor requestMappingStoreProcessor;

    @Autowired
    public RemoteRequestMappingGatherListener(
            RequestMappingStoreProcessor requestMappingStoreProcessor) {
        this.requestMappingStoreProcessor = requestMappingStoreProcessor;
    }

    @Override
    public void onApplicationEvent(RemoteRequestMappingGatherEvent event) {

        log.info(" Request mapping gather REMOTE listener, response event!");

        String requestMapping = event.getData();
        log.debug(" Fetch data [{}]", requestMapping);
        if (ObjectUtils.isNotEmpty(requestMapping)) {
            List<RequestMapping> requestMappings =
                    JsonUtils.toList(requestMapping, RequestMapping.class);
            if (CollectionUtils.isNotEmpty(requestMappings)) {
                requestMappingStoreProcessor.postProcess(requestMappings);
            }
        }
    }
}
