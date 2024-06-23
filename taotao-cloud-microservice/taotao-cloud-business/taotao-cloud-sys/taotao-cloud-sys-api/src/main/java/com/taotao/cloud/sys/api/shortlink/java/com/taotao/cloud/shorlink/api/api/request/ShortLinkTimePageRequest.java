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

package com.taotao.cloud.sys.api.shortlink.java.com.taotao.cloud.shorlink.api.api.request;

import com.taotao.cloud.shorlink.api.api.common.PageRequest;
import com.taotao.cloud.shorlink.api.api.common.Range;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * This is Description
 *
 * @since 2022/05/04
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShortLinkTimePageRequest extends PageRequest {

    /** 时间范围 */
    private Range<LocalDateTime> dateRange;

    /**
     * 查询类型： 0（null） = 创建时间、1 = 更新时间
     *
     * @see BooleanEnum
     */
    private Integer queryType;

    /** 倒序： 0（null） = 升序、1 = 降序 */
    private Integer desc;
}
