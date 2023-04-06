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

package com.taotao.cloud.message.biz.web;

import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.sms.common.model.NoticeInfo;
import com.taotao.cloud.sms.common.model.VerifyInfo;
import java.lang.reflect.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * 短信控制器路径自动配置
 *
 * @author shuigedeng
 */
@AutoConfiguration(after = SmsWebmvcAutoConfiguration.class)
@ConditionalOnProperty(prefix = SmsWebmvcProperties.PREFIX, name = "enable", havingValue = "true")
public class SmsWebmvcPathAutoConfiguration {

    private static String getBasePath(SmsWebmvcProperties properties) {
        String bathPath = StringUtils.trimToNull(properties.getBasePath());

        return bathPath == null ? SmsWebmvcProperties.DEFAULT_BASE_PATH : bathPath;
    }

    /**
     * 构造短信Controller
     *
     * @param controller 短信Controller
     * @param mapping RequestMappingHandlerMapping
     * @param properties 短信Web配置
     * @throws NoSuchMethodException if a matching method is not found or if the name is
     *     "&lt;init&gt;"or "&lt;clinit&gt;".
     * @throws SecurityException If a security manager, <i>s</i>, is present and the caller's class
     *     loader is not the same as or an ancestor of the class loader for the current class and
     *     invocation of {@link SecurityManager#checkPackageAccess s.checkPackageAccess()} denies
     *     access to the package of this class.
     */
    @Autowired
    @ConditionalOnBean(RequestMappingHandlerMapping.class)
    public void smsController(
            SmsWebmvcProperties properties, RequestMappingHandlerMapping mapping, SmsController controller)
            throws NoSuchMethodException, SecurityException {

        String bathPath = getBasePath(properties);

        if (properties.isEnableSend()) {
            Method sendMethod = SmsController.class.getMethod("sendVerificationCode", String.class);
            RequestMappingInfo sendInfo = RequestMappingInfo.paths(bathPath + "/verificationCode/{phone}")
                    .methods(RequestMethod.POST)
                    .build();
            mapping.registerMapping(sendInfo, controller, sendMethod);
            LogUtils.debug("registerMapping: {}", sendInfo);
        } else {
            LogUtils.debug("not register: sendInfo");
        }

        if (properties.isEnableGet()) {
            Method getMethod = SmsController.class.getMethod("getVerificationCode", String.class, String.class);
            RequestMappingInfo getInfo = RequestMappingInfo.paths(bathPath + "/verificationCode/{phone}")
                    .methods(RequestMethod.GET)
                    .produces("application/json")
                    .build();
            mapping.registerMapping(getInfo, controller, getMethod);
            LogUtils.debug("registerMapping: {}", getInfo);
        } else {
            LogUtils.debug("not register: getInfo");
        }

        if (properties.isEnableVerify()) {
            Method verifyMethod = SmsController.class.getMethod("verifyVerificationCode", VerifyInfo.class);
            RequestMappingInfo verifyInfo = RequestMappingInfo.paths(bathPath + "/verificationCode")
                    .methods(RequestMethod.POST)
                    .build();
            mapping.registerMapping(verifyInfo, controller, verifyMethod);
            LogUtils.debug("registerMapping: {}", verifyInfo);
        } else {
            LogUtils.debug("not register: verifyInfo");
        }

        if (properties.isEnableNotice()) {
            Method noticeMethod = SmsController.class.getMethod("sendNotice", NoticeInfo.class);
            RequestMappingInfo noticeInfo = RequestMappingInfo.paths(bathPath + "/notice")
                    .methods(RequestMethod.PUT)
                    .build();
            mapping.registerMapping(noticeInfo, controller, noticeMethod);
            LogUtils.debug("registerMapping: {}", noticeInfo);
        } else {
            LogUtils.debug("not register: noticeInfo");
        }
    }
}
