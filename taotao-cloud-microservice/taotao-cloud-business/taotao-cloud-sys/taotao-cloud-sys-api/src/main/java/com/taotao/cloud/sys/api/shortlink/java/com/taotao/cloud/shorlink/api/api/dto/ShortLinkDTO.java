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

package com.taotao.cloud.sys.api.shortlink.java.com.taotao.cloud.shorlink.api.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
@Builder
public class ShortLinkDTO implements Serializable {

    /** ID */
    private Long id;

    /** 分组ID */
    private Long groupId;

    /** 短链标题 */
    private String title;

    /** 原始URL */
    private String originUrl;

    /** 短链域名 */
    private String domain;

    /** 短链码 */
    private String code;

    /** 账户编码 */
    private Long accountNo;

    /**
     * 状态：0=无效、1=有效
     *
     * @see com.zc.shortlink.api.enums.BooleanEnum
     */
    private Integer state;

    /** 过期时间 */
    private LocalDate expired;
}
