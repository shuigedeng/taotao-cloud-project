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

import com.taotao.cloud.auth.biz.metadata.event.SysAttributeChangeEvent;
import com.taotao.cloud.auth.biz.metadata.processor.SecurityMetadataDistributeProcessor;
import com.taotao.cloud.auth.biz.strategy.user.SysAttribute;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * <p>SysSecurityAttribute变更事件监听 </p>
 *
 */
@Component
public class SysAttributeChangeListener implements ApplicationListener<SysAttributeChangeEvent> {

    private static final Logger log = LoggerFactory.getLogger(SysAttributeChangeListener.class);

    private final SecurityMetadataDistributeProcessor securityMetadataDistributeProcessor;

    public SysAttributeChangeListener(
            SecurityMetadataDistributeProcessor securityMetadataDistributeProcessor) {
        this.securityMetadataDistributeProcessor = securityMetadataDistributeProcessor;
    }

    @Override
    public void onApplicationEvent(SysAttributeChangeEvent event) {

        log.debug(" SysAttribute Change Listener, response event!");

        SysAttribute sysAttribute = event.getData();
        if (ObjectUtils.isNotEmpty(sysAttribute)) {
            log.debug(" Got SysAttribute, start to process SysAttribute change.");
            securityMetadataDistributeProcessor.distributeChangedSecurityAttribute(sysAttribute);
        }
    }
}
