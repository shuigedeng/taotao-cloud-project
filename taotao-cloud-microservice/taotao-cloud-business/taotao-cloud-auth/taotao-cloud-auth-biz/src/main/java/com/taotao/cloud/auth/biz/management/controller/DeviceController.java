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

package com.taotao.cloud.auth.biz.management.controller;

import com.taotao.boot.security.spring.constants.DefaultConstants;
import com.taotao.boot.security.spring.constants.SymbolConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>设备激活 </p>
 *
 *
 * @since : 2023/3/24 17:09
 */
@Controller
public class DeviceController {

    @GetMapping(DefaultConstants.DEVICE_ACTIVATION_URI)
    public String activate(
            @RequestParam(value = OAuth2ParameterNames.USER_CODE, required = false)
                    String userCode) {
        if (StringUtils.isNotBlank(userCode)) {
            return "redirect:"
                    + DefaultConstants.DEVICE_VERIFICATION_ENDPOINT
                    + SymbolConstants.QUESTION
                    + OAuth2ParameterNames.USER_CODE
                    + SymbolConstants.EQUAL
                    + userCode;
        }
        return "activation";
    }

    @GetMapping(value = DefaultConstants.DEVICE_VERIFICATION_SUCCESS_URI)
    public String activated() {
        return "activation-allowed";
    }
}
