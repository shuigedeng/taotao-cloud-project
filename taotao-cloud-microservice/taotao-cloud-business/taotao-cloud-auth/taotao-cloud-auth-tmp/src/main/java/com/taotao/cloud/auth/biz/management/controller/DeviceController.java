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

package com.taotao.cloud.auth.biz.management.controller;

import com.taotao.cloud.security.springsecurity.authorization.configuration.DefaultConstants;
import com.taotao.cloud.security.springsecurity.core.constants.SymbolConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>Description: 设备激活 </p>
 *
 * @author : gengwei.zheng
 * @date : 2023/3/24 17:09
 */
@Controller
public class DeviceController {

    @GetMapping(DefaultConstants.DEVICE_ACTIVATION_URI)
    public String activate(@RequestParam(value = OAuth2ParameterNames.USER_CODE, required = false) String userCode) {
        if (StringUtils.isNotBlank(userCode)) {
            return "redirect:" + DefaultConstants.DEVICE_VERIFICATION_ENDPOINT + SymbolConstants.QUESTION + OAuth2ParameterNames.USER_CODE + SymbolConstants.EQUAL + userCode;
        }
        return "activation";
    }

    @GetMapping(value = DefaultConstants.DEVICE_VERIFICATION_SUCCESS_URI)
    public String activated() {
        return "activation-allowed";
    }
}
