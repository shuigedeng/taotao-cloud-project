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

package com.taotao.cloud.shorlink.api.api.request;

import lombok.*;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.*;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * This is Description
 *
 * @since 2022/05/03
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
public class ShortLinkCreateRequest implements Serializable {

    /** 用户ID */
    private Long accountId;

    /** 组 */
    private Long groupId;

    /** 短链标题 */
    private String title;

    /** 原生url */
    private String originalUrl;

    /** 域名id */
    private Long domainId;

    /**
     * 域名类型
     *
     * @see ShortLinkDomainTypeEnum
     */
    private Integer domainType;

    /** 过期时间 */
    private LocalDate expired;
}
