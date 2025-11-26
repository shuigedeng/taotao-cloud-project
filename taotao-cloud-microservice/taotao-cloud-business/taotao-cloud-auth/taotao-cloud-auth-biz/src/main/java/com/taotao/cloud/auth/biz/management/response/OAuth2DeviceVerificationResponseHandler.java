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

package com.taotao.cloud.auth.biz.management.response;

import com.taotao.boot.security.spring.constants.DefaultConstants;
import com.taotao.cloud.auth.biz.management.service.OAuth2DeviceService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.taotao.boot.common.utils.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2DeviceVerificationAuthenticationToken;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

/**
 * <p>设备验证成功后续逻辑处理器 </p>
 *
 *
 * @since : 2023/5/3 9:35
 */
public class OAuth2DeviceVerificationResponseHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final Logger log =
            LoggerFactory.getLogger(OAuth2DeviceVerificationResponseHandler.class);

    private final OAuth2DeviceService deviceService;

    public OAuth2DeviceVerificationResponseHandler(OAuth2DeviceService deviceService) {
        super(DefaultConstants.DEVICE_VERIFICATION_SUCCESS_URI);
        this.deviceService = deviceService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        OAuth2DeviceVerificationAuthenticationToken deviceVerificationAuthenticationToken =
                (OAuth2DeviceVerificationAuthenticationToken) authentication;

        log.info(
                "Device verification authentication token is : [{}]",
                deviceVerificationAuthenticationToken);

        String clientId = deviceVerificationAuthenticationToken.getClientId();

        if (StringUtils.isNotBlank(clientId)) {
            boolean success = deviceService.activate(clientId, true);
            log.info("The activation status of the device is : [{}]", success);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
