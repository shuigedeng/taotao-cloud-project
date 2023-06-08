/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.dante.authorization.listener;

import com.taotao.cloud.auth.biz.dante.authorization.processor.SecurityMetadataSourceAnalyzer;
import com.taotao.cloud.auth.biz.dante.core.definition.domain.SecurityAttribute;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.bus.ServiceMatcher;
import org.springframework.context.ApplicationListener;

import java.util.List;

/**
 * <p>Description: Security Metadata 数据同步监听 </p>
 *
 * @author : gengwei.zheng
 * @date : 2021/8/6 12:23
 */
public class RemoteSecurityMetadataSyncListener implements ApplicationListener<RemoteSecurityMetadataSyncEvent> {

    private static final Logger log = LoggerFactory.getLogger(RemoteSecurityMetadataSyncListener.class);

    private final SecurityMetadataSourceAnalyzer securityMetadataSourceAnalyzer;
    private final ServiceMatcher serviceMatcher;

    public RemoteSecurityMetadataSyncListener(SecurityMetadataSourceAnalyzer securityMetadataSourceAnalyzer, ServiceMatcher serviceMatcher) {
        this.securityMetadataSourceAnalyzer = securityMetadataSourceAnalyzer;
        this.serviceMatcher = serviceMatcher;
    }

    @Override
    public void onApplicationEvent(RemoteSecurityMetadataSyncEvent event) {

        if (!serviceMatcher.isFromSelf(event)) {
            log.info("[Herodotus] |- Remote security metadata sync listener, response event!");

            String data = event.getData();
            if (StringUtils.isNotBlank(data)) {
                List<SecurityAttribute> securityMetadata = Jackson2Utils.toList(data, SecurityAttribute.class);

                if (CollectionUtils.isNotEmpty(securityMetadata)) {
                    log.debug("[Herodotus] |- Got security attributes from service [{}], current [{}] start to process security attributes.", event.getOriginService(), event.getDestinationService());
                    securityMetadataSourceAnalyzer.processSecurityAttribute(securityMetadata);
                }
            }
        }
    }
}
