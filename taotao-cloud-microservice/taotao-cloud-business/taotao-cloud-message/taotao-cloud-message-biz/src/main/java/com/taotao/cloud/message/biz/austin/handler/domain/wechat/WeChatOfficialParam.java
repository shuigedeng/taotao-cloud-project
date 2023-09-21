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

package com.taotao.cloud.message.biz.austin.handler.domain.wechat;

import java.util.Map;
import java.util.Set;
import lombok.Builder;
import lombok.Data;

/**
 * @author sunql
 * @since 2022年05月06日 9:56
 *     <p>服务号参数
 */
@Data
@Builder
public class WeChatOfficialParam {
    /** 业务Id */
    private Long messageTemplateId;

    /** 关注服务号得用户 */
    private Set<String> openIds;

    /** 模板消息的信息载体 */
    private Map<String, String> data;

    /** 发送账号 */
    private Integer sendAccount;
}
