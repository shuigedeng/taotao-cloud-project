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

package com.taotao.cloud.message.biz.austin.web.vo;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息模板管理 请求参数
 *
 * @author 3y
 * @date 2022/1/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageTemplateParam {

    /** 当前页码 */
    @NotNull
    private Integer page = 1;

    /** 当前页大小 */
    @NotNull
    private Integer perPage = 10;

    /** 模板ID */
    private Long id;

    /** 当前用户 */
    private String creator;

    /** 消息接收者(测试发送时使用) */
    private String receiver;

    /** 下发参数信息 */
    private String msgContent;

    /** 模版名称 */
    private String keywords;
}
