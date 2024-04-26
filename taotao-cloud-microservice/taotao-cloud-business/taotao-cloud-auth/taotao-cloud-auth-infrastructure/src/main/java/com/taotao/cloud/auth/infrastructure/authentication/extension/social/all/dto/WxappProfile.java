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

package com.taotao.cloud.auth.infrastructure.authentication.extension.social.all.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * <p>微信小程序登录请求实体 </p>
 *
 *
 * @since : 2022/1/26 14:44
 */
@Schema(name = "微信小程序登录请求实体", title = "根据code和appid返回微信小程序session信息")
public class WxappProfile {

    @Schema(name = "code", title = "前端调用小程序自己的方法返回的code")
    @NotBlank(message = "微信小程序code参数不能为空")
    private String code;

    @Schema(name = "appId", title = "需要前端返回给后端appId，以支持多个小程序")
    @NotBlank(message = "微信小程序appId参数不能为空")
    private String appId;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}
