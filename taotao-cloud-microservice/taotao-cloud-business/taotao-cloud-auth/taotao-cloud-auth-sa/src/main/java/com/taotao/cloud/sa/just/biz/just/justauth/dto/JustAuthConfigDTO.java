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

import lombok.Data;
import lombok.experimental.*;
import lombok.EqualsAndHashCode;

/**
 * 租户第三方登录功能配置表
 *
 * @since 2022-05-16
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "JustAuthConfigDTO对象", description = "租户第三方登录功能配置表")
public class JustAuthConfigDTO {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;

    @Schema(description = "登录开关")
    private Boolean enabled;

    @Schema(description = "配置类")
    private String enumClass;

    @Schema(description = "Http超时")
    private Integer httpTimeout;

    @Schema(description = "缓存类型")
    private String cacheType;

    @Schema(description = "缓存前缀")
    private String cachePrefix;

    @Schema(description = "缓存超时")
    private Integer cacheTimeout;

    @Schema(description = "状态")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}
