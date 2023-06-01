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

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

/**
 * 租户第三方登录功能配置表
 *
 * @since 2022-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "JustAuthConfig对象", description = "租户第三方登录功能配置表")
public class CreateJustAuthConfigDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "登录开关")
    @NotBlank(message = "登录开关不能为空")
    @Length(min = 1, max = 1)
    private Boolean enabled;

    @Schema(description = "配置类")
    @Length(min = 1, max = 255)
    private String enumClass;

    @Schema(description = "Http超时")
    @Min(0L)
    @Max(2147483647L)
    @Length(min = 1, max = 19)
    private Integer httpTimeout;

    @Schema(description = "缓存类型")
    @Length(min = 1, max = 32)
    private String cacheType;

    @Schema(description = "缓存前缀")
    @Length(min = 1, max = 100)
    private String cachePrefix;

    @Schema(description = "缓存超时")
    @Min(0L)
    @Max(2147483647L)
    @Length(min = 1, max = 255)
    private Integer cacheTimeout;

    @Schema(description = "状态")
    @NotBlank(message = "状态不能为空")
    @Min(0L)
    @Max(2147483647L)
    @Length(min = 1, max = 3)
    private Integer status;

    @Schema(description = "备注")
    @Length(min = 1, max = 255)
    private String remark;
}
