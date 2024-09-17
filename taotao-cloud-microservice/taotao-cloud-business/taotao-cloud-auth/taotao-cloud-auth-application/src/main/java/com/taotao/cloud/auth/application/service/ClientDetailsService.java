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

package com.taotao.cloud.auth.application.service;

import com.taotao.boot.security.spring.authority.TtcGrantedAuthority;
import java.util.Set;

/**
 * <p>客户端操作基础接口 </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-04 10:05:31
 */
public interface ClientDetailsService {

    /**
     * 根据客户端ID获取客户端权限
     *
     * @param clientId 客户端ID
     * @return {@link Set }<{@link TtcGrantedAuthority }>
     * @since 2023-07-04 10:05:31
     */
    Set<TtcGrantedAuthority> findAuthoritiesById(String clientId);
}
