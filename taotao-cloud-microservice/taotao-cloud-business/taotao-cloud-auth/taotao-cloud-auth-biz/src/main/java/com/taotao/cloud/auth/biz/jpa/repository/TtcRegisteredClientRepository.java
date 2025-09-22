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

package com.taotao.cloud.auth.biz.jpa.repository;

import com.taotao.boot.data.jpa.base.repository.JpaSuperRepository;
import com.taotao.cloud.auth.biz.jpa.entity.TtcRegisteredClient;
import jakarta.persistence.QueryHint;
import java.util.Optional;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.QueryHints;

/**
 * <p>TtcRegisteredClientRepository </p>
 *
 * @author shuigedeng
 * @version 2023.07
 * @since 2023-07-10 17:11:24
 */
public interface TtcRegisteredClientRepository
        extends JpaSuperRepository<TtcRegisteredClient, String> {

    /**
     * 根据 ClientId 查询 RegisteredClient
     *
     * @param clientId OAuth2 客户端ID
     * @return {@link Optional }<{@link TtcRegisteredClient }>
     * @since 2023-07-10 17:11:24
     */
    @QueryHints(@QueryHint(name = AvailableHints.HINT_CACHEABLE, value = "true"))
    Optional<TtcRegisteredClient> findByClientId(String clientId);
}
