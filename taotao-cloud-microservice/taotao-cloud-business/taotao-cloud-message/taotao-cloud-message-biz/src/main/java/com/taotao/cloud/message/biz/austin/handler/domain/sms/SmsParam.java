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

package com.taotao.cloud.message.biz.austin.handler.domain.sms;

import java.util.Set;
import lombok.Builder;
import lombok.Data;

/**
 * @author 3y
 * @date 2021/11/4 发送短信参数
 */
@Data
@Builder
public class SmsParam {

    /** 业务Id */
    private Long messageTemplateId;

    /** 需要发送的手机号 */
    private Set<String> phones;

    /**
     * 发送账号的id（如果短信模板指定了发送账号，则该字段有值）
     *
     * <p>如果有账号id，那就用账号id 检索 如果没有账号id，那就根据
     * com.taotao.cloud.message.biz.austin.handler.domain.sms.SmsParam#scriptName 检索
     */
    private Integer sendAccountId;

    /** 渠道账号的脚本名标识 */
    private String scriptName;

    /** 发送文案 */
    private String content;
}
