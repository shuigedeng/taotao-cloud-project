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
import java.util.Set;

/**
 * 短链服务 - 批量查询短链 - Request
 *
 * @since 2022/05/04
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain=true)
public class ShortLinkListRequest implements Serializable {

    private Set<String> shortLinkCodeSet;
}
