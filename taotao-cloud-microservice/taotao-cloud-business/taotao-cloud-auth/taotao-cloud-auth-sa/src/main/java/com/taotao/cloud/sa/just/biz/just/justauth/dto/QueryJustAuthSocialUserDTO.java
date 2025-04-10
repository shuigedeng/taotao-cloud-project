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

package com.taotao.cloud.sa.just.biz.just.justauth.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 第三方用户绑定
 *
 * @since 2022-05-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "JustAuthSocialUser对象", description = "第三方用户绑定")
public class QueryJustAuthSocialUserDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户id")
    @Length(min = 1, max = 19)
    private Long userId;

    @Schema(description = "第三方用户id")
    @Length(min = 1, max = 19)
    private Long socialId;

    @Schema(description = "开始时间")
    private String beginDateTime;

    @Schema(description = "结束时间")
    private String endDateTime;
}
