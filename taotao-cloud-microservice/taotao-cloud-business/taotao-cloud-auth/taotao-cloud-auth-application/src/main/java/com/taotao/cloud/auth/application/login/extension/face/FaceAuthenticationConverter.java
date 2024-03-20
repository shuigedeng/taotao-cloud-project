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

package com.taotao.cloud.auth.application.login.extension.face;

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
public class FaceAuthenticationConverter implements Converter<HttpServletRequest, FaceAuthenticationToken> {
    public static final String SPRING_SECURITY_FORM_IMAGE_BASE_64_KEY = "imgBase64";
    private String imgBase64Parameter = SPRING_SECURITY_FORM_IMAGE_BASE_64_KEY;

    @Override
    public FaceAuthenticationToken convert(HttpServletRequest request) {
        MultiValueMap<String, String> parameters = ExtensionLoginUtils.getParameters(request);

        // imgBase64 (REQUIRED)
        ExtensionLoginUtils.checkRequiredParameter(parameters, imgBase64Parameter);
        String imgBase64 = request.getParameter(this.imgBase64Parameter);

        return new FaceAuthenticationToken(imgBase64);
    }

    public String getImgBase64Parameter() {
        return imgBase64Parameter;
    }

    public void setImgBase64Parameter(String imgBase64Parameter) {
        this.imgBase64Parameter = imgBase64Parameter;
    }
}
