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

package com.taotao.cloud.auth.infrastructure.extension.qrcocde.tmp1;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CodeData {
    /**
     * 二维码状态
     */
    private CodeStatusEnum codeStatus;

    /**
     * 提示消息
     */
    private String message;

    /**
     * 正式 token
     */
    private String token;

    public CodeData(CodeStatusEnum codeStatus) {
        this.codeStatus = codeStatus;
    }

    public CodeData(CodeStatusEnum codeStatus, String message) {
        this.codeStatus = codeStatus;
        this.message = message;
    }

    public CodeData(CodeStatusEnum codeStatus, String message, String token) {
        this.codeStatus = codeStatus;
        this.message = message;
        this.token = token;
    }
}
