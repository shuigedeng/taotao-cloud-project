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

package com.taotao.cloud.auth.biz.demo.core.definition.details;

import cn.herodotus.engine.oauth2.core.utils.SymmetricUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

/**
 * Description: 表单登录 Details 定义
 *
 * @author : gengwei.zheng
 * @date : 2022/4/12 10:32
 */
public class FormLoginWebAuthenticationDetails extends WebAuthenticationDetails {

    /** 验证码是否关闭 */
    private final Boolean closed;
    /** 请求中，验证码对应的表单参数名。对应UI Properties 里面的 captcha parameter */
    private final String parameterName;
    /** 验证码分类 */
    private final String category;

    private String code = null;
    private String identity = null;

    public FormLoginWebAuthenticationDetails(
            String remoteAddress,
            String sessionId,
            Boolean closed,
            String parameterName,
            String category,
            String code,
            String identity) {
        super(remoteAddress, sessionId);
        this.closed = closed;
        this.parameterName = parameterName;
        this.category = category;
        this.code = code;
        this.identity = identity;
    }

    public FormLoginWebAuthenticationDetails(
            HttpServletRequest request, boolean closed, String parameterName, String category) {
        super(request);
        this.closed = closed;
        this.parameterName = parameterName;
        this.category = category;
        this.init(request);
    }

    private void init(HttpServletRequest request) {
        String encryptedCode = request.getParameter(parameterName);
        String key = request.getParameter("symmetric");

        HttpSession session = request.getSession();
        this.identity = session.getId();

        if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(encryptedCode)) {
            byte[] byteKey = SymmetricUtils.getDecryptedSymmetricKey(key);
            this.code = SymmetricUtils.decrypt(encryptedCode, byteKey);
        }
    }

    public Boolean getClosed() {
        return closed;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getCategory() {
        return category;
    }

    public String getCode() {
        return code;
    }

    public String getIdentity() {
        return identity;
    }
}
