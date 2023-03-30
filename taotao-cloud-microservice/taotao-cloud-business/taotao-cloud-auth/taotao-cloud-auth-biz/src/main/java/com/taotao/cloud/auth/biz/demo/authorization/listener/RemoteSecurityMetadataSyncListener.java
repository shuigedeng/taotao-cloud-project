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

package com.taotao.cloud.auth.biz.demo.authorization.listener;

import cn.herodotus.engine.assistant.core.json.jackson2.utils.JacksonUtils;
import cn.herodotus.engine.event.security.remote.RemoteSecurityMetadataSyncEvent;
import cn.herodotus.engine.oauth2.authorization.processor.SecurityMetadataSourceAnalyzer;
import cn.herodotus.engine.oauth2.core.definition.domain.SecurityAttribute;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.context.ApplicationListener;

/**
 * Description: Security Metadata 数据同步监听
 *
 * @author : gengwei.zheng
 * @date : 2021/8/6 12:23
 */
public class RemoteSecurityMetadataSyncListener
        implements ApplicationListener<RemoteSecurityMetadataSyncEvent> {

    private static final Logger log =
            LoggerFactory.getLogger(RemoteSecurityMetadataSyncListener.class);

    private final SecurityMetadataSourceAnalyzer securityMetadataSourceAnalyzer;
    private final ServiceMatcher serviceMatcher;

    public RemoteSecurityMetadataSyncListener(
            SecurityMetadataSourceAnalyzer securityMetadataSourceAnalyzer,
            ServiceMatcher serviceMatcher) {
        this.securityMetadataSourceAnalyzer = securityMetadataSourceAnalyzer;
        this.serviceMatcher = serviceMatcher;
    }

    @Override
    public void onApplicationEvent(RemoteSecurityMetadataSyncEvent event) {

        if (!serviceMatcher.isFromSelf(event)) {
            log.info("[Herodotus] |- Remote security metadata sync listener, response event!");

            String data = event.getData();
            if (StringUtils.isNotBlank(data)) {
                List<SecurityAttribute> securityAttributes =
                        JacksonUtils.toList(data, SecurityAttribute.class);

                if (CollectionUtils.isNotEmpty(securityAttributes)) {
                    log.debug(
                            "[Herodotus] |- Got security attributes from service [{}], current [{}]"
                                    + " start to process security attributes.",
                            event.getOriginService(),
                            event.getDestinationService());
                    securityMetadataSourceAnalyzer.processSecurityMetadata(securityAttributes);
                }
            }
        }
    }
}
