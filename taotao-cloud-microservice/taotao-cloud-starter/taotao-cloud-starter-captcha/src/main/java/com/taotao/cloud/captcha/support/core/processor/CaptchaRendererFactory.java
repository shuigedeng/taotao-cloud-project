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

package com.taotao.cloud.captcha.support.core.processor;

import com.taotao.cloud.captcha.support.core.definition.Renderer;
import com.taotao.cloud.captcha.support.core.definition.enums.CaptchaCategory;
import com.taotao.cloud.captcha.support.core.dto.Captcha;
import com.taotao.cloud.captcha.support.core.dto.Verification;
import com.taotao.cloud.captcha.support.core.exception.CaptchaCategoryIsIncorrectException;
import com.taotao.cloud.captcha.support.core.exception.CaptchaHandlerNotExistException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>Description: Captcha 工厂 </p>
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-12 12:58:15
 */
@Component
public class CaptchaRendererFactory {

    @Autowired
    private final Map<String, Renderer> handlers = new ConcurrentHashMap<>(8);

    public Renderer getRenderer(String category) {
        CaptchaCategory captchaCategory = CaptchaCategory.getCaptchaCategory(category);

        if (ObjectUtils.isEmpty(captchaCategory)) {
            throw new CaptchaCategoryIsIncorrectException("Captcha category is incorrect.");
        }

        Renderer renderer = handlers.get(captchaCategory.getConstant());
        if (ObjectUtils.isEmpty(renderer)) {
            throw new CaptchaHandlerNotExistException("");
        }

        return renderer;
    }

    public Captcha getCaptcha(String identity, String category) {
        Renderer renderer = getRenderer(category);
        return renderer.getCapcha(identity);
    }

    public boolean verify(Verification verification) {
        Renderer renderer = getRenderer(verification.getCategory());
        return renderer.verify(verification);
    }
}
