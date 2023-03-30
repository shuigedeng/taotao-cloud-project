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

package com.taotao.cloud.message.biz.austin.handler.domain.alipay;

import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

/**
 * @author jwq 支付宝小程序参数
 */
@Data
@Builder
public class AlipayMiniProgramParam {

    /** 业务Id */
    private Long messageTemplateId;

    /** 发送账号 */
    private Integer sendAccount;

    /** 接收者（用户）的 UserId */
    private Set<String> toUserId;

    /** 模板内容，格式形如 { "key1": { "value": any }, "key2": { "value": any } } */
    private Map<String, String> data;

    //    /**
    //     * 支付消息模板：需传入用户发生的交易行为的支付宝交易号 trade_no；
    //     * 表单提交模板：需传入用户在小程序触发表单提交事件获得的表单号；
    //     * 刷脸消息模板：需传入在IOT刷脸后得到的ftoken等，用于信息发送的校验。
    //     * 说明：订阅消息模板无需传入本参数。
    //     */
    //    private String formId;
}
