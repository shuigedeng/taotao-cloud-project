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

package com.taotao.cloud.auth.biz.authentication.login.extension.email;

import com.taotao.cloud.auth.biz.authentication.utils.ExtensionLoginUtils;
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
public class EmailAuthenticationConverter implements Converter<HttpServletRequest, EmailAuthenticationToken> {
    /**
     * 默认的请求参数名称
     */
    public static final String EMAIL_KEY = "email";

    public static final String EMAIL_CODE_KEY = "emailCode";

    private String emailParameter = EMAIL_KEY;
    private String emailCodeParameter = EMAIL_CODE_KEY;

    @Override
    public EmailAuthenticationToken convert(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = ExtensionLoginUtils.getParameters(request);
        // username (REQUIRED)
        ExtensionLoginUtils.checkRequiredParameter(parameters, emailParameter);
        // password (REQUIRED)
        ExtensionLoginUtils.checkRequiredParameter(parameters, emailCodeParameter);

        String email = request.getParameter(this.emailParameter);
        String emailCode = request.getParameter(this.emailCodeParameter);

        return EmailAuthenticationToken.unauthenticated(email, emailCode);
    }

    public String getEmailParameter() {
        return emailParameter;
    }

    public void setEmailParameter(String emailParameter) {
        this.emailParameter = emailParameter;
    }

    public String getEmailCodeParameter() {
        return emailCodeParameter;
    }

    public void setEmailCodeParameter(String emailCodeParameter) {
        this.emailCodeParameter = emailCodeParameter;
    }
}
