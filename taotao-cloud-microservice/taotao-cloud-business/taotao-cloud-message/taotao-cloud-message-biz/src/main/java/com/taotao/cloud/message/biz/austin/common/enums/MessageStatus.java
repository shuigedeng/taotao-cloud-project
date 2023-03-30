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

package com.taotao.cloud.message.biz.austin.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

/**
 * @author 3y
 * @date 2022/1/22
 */
@Getter
@ToString
@AllArgsConstructor
public enum MessageStatus {

    /** 10.新建 */
    INIT(10, "初始化状态"),
    /** 20.停用 */
    STOP(20, "停用"),
    /** 30.启用 */
    RUN(30, "启用"),
    /** 40.等待发送 */
    PENDING(40, "等待发送"),
    /** 50.发送中 */
    SENDING(50, "发送中"),
    /** 60.发送成功 */
    SEND_SUCCESS(60, "发送成功"),
    /** 70.发送失败 */
    SEND_FAIL(70, "发送失败");

    private final Integer code;
    private final String description;
}
