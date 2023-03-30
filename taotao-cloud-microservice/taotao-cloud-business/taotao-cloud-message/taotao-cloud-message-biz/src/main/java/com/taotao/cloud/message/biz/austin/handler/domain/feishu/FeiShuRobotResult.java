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

package com.taotao.cloud.message.biz.austin.handler.domain.feishu;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 3y 飞书机器人 返回值
 */
@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class FeiShuRobotResult {

    /** extra */
    @JSONField(name = "Extra")
    private Object extra;
    /** statusCode */
    @JSONField(name = "StatusCode")
    private Integer statusCode;
    /** statusMessage */
    @JSONField(name = "StatusMessage")
    private String statusMessage;
    /** code */
    @JSONField(name = "code")
    private Integer code;
    /** msg */
    @JSONField(name = "msg")
    private String msg;
    /** data */
    @JSONField(name = "data")
    private DataDTO data;

    /** DataDTO */
    @NoArgsConstructor
    @Data
    public static class DataDTO {}
}
