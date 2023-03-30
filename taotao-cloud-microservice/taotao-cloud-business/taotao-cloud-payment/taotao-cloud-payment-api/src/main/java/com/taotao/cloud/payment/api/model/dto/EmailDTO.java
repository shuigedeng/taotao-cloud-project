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

package com.taotao.cloud.payment.api.model.dto;

import io.soabase.recordbuilder.core.RecordBuilder;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;

/**
 * EmailDTO
 *
 * @author shuigedeng
 * @version 2022.05
 * @since 2022-05-26 09:48
 */
@RecordBuilder
public record EmailDTO(

        /** 收件人 */
        @NotBlank(message = "收件人不能为空") String fromUser,

        /** 邮件服务器SMTP地址 */
        String host,

        /** 密码 */
        String pass,

        /** 端口 */
        @Max(value = 200, message = "端口不能大于200") Integer port,

        /** 发件者用户名 */
        String user) {}
