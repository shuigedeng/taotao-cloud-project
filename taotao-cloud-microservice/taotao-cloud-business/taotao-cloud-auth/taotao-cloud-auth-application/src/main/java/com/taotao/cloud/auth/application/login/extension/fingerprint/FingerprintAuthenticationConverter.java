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

package com.taotao.cloud.auth.application.login.extension.fingerprint;

import com.taotao.cloud.auth.application.utils.ExtensionLoginUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.MultiValueMap;

/**
 * 帐户验证转换器
 *
 * @author shuigedeng
 * @version 2023.04
 * @since 2023-06-29 13:07:23
 */
public class FingerprintAuthenticationConverter
        implements Converter<HttpServletRequest, FingerprintAuthenticationToken> {
    public static final String SPRING_SECURITY_FORM_USERNAME_KEY = "name";
    public static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "fingerPrint";

    private String usernameParameter = SPRING_SECURITY_FORM_USERNAME_KEY;
    private String fingerPrintParameter = SPRING_SECURITY_FORM_PASSWORD_KEY;

    @Override
    public FingerprintAuthenticationToken convert(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = ExtensionLoginUtils.getParameters(request);

        // username (REQUIRED)
        ExtensionLoginUtils.checkRequiredParameter(parameters, usernameParameter);
        // password (REQUIRED)
        ExtensionLoginUtils.checkRequiredParameter(parameters, fingerPrintParameter);

        String username = request.getParameter(this.usernameParameter);
        String fingerPrint = request.getParameter(this.fingerPrintParameter);

        return FingerprintAuthenticationToken.unauthenticated(username, fingerPrint);
    }

    public String getUsernameParameter() {
        return usernameParameter;
    }

    public void setUsernameParameter(String usernameParameter) {
        this.usernameParameter = usernameParameter;
    }

    public String getFingerPrintParameter() {
        return fingerPrintParameter;
    }

    public void setFingerPrintParameter(String fingerPrintParameter) {
        this.fingerPrintParameter = fingerPrintParameter;
    }
}
