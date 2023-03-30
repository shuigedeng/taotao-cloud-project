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

package com.taotao.cloud.message.biz.austin.common.dto.account.sms;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 3y
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsAccount {

    /** 标识渠道商Id */
    protected Integer supplierId;

    /** 标识渠道商名字 */
    protected String supplierName;

    /** 【重要】类名，定位到具体的处理"下发"/"回执"逻辑 依据ScriptName对应具体的某一个短信账号 */
    protected String scriptName;
}
