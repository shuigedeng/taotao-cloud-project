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

package com.taotao.cloud.gateway.model;

import java.io.Serializable;
import java.net.URI;
import java.time.LocalDateTime;

import lombok.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.*;

/**
 * AccessRecord
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessRecord implements Serializable {

    private String formData;
    private URI targetUri;
    private String method;
    private String scheme;
    private String path;
    private String body;
    private String ip;
    private Integer status;
    private Long userId;
    private Long consumingTime;
    private LocalDateTime createTime;
}
