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

package com.taotao.cloud.im.biz.platform.modules.auth.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthVo03 {

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /** 验证码 */
    @NotBlank(message = "验证码不能为空")
    private String code;

    /** 推送ID */
    @NotBlank(message = "推送ID不能为空")
    private String cid;
}
