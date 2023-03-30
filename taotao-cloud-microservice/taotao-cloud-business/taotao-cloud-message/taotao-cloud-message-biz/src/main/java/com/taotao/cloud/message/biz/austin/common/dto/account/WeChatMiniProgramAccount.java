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

package com.taotao.cloud.message.biz.austin.common.dto.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 小程序订阅消息参数
 *
 * <p>参数示例：
 * https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/subscribe-message/subscribeMessage.send.html
 * * @author sunql
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WeChatMiniProgramAccount {

    /** 订阅消息模板ID */
    private String templateId;

    /** 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版 */
    private String miniProgramState;

    /** 击模板卡片后的跳转页面，仅限本小程序内的页面。支持带参数,（示例index?foo=bar）。该字段不填则模板无跳转。 */
    private String page;

    /** 账号相关 */
    private String appId;

    private String appSecret;
    private String grantType;
}
