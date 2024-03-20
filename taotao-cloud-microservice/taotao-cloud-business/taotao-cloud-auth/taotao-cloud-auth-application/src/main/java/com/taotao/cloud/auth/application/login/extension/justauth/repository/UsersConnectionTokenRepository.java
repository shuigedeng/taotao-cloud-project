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

package com.taotao.cloud.auth.application.login.extension.justauth.repository;

import com.taotao.cloud.security.justauth.justauth.AuthTokenPo;
import com.taotao.cloud.security.justauth.justauth.EnableRefresh;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * A data access interface for managing a global store of users connections token to service providers.
 * Provides data access operations.
 * @author YongWu zheng
 * @version V2.0  Created by 2020-10-08 20:10
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface UsersConnectionTokenRepository {

    /**
     * 根据 tokenId 获取 tokenId
     * @param tokenId   tokenId
     * @return  AuthTokenPo
     * @throws Exception 查询错误
     */
    @Nullable
    AuthTokenPo findAuthTokenById(@NonNull String tokenId) throws Exception;

    /**
     * 持久化 authToken, 并把返回的 authToken id 保存在 authToken 中
     * @param authToken     authToken
     * @return  AuthTokenPo
     * @throws Exception    持久化 authToken 异常
     */
    @NonNull
    AuthTokenPo saveAuthToken(@NonNull AuthTokenPo authToken) throws Exception;

    /**
     * 更新 {@link AuthTokenPo}
     * @param authToken     更新 {@link AuthTokenPo}
     * @return  AuthTokenPo
     * @throws Exception    数据更新异常
     */
    @NonNull
    AuthTokenPo updateAuthToken(@NonNull AuthTokenPo authToken) throws Exception;

    /**
     * 删除 id = tokenId 的记录
     * @param tokenId   tokenId
     * @throws Exception 删除错误
     */
    void delAuthTokenById(@NonNull String tokenId) throws Exception;

    /**
     * 获取最大 TokenId
     * @return 获取最大 TokenId
     * @throws Exception sql 执行错误
     */
    @NonNull
    Long getMaxTokenId() throws Exception;

    /**
     * 获取 ID 范围在 startId(包含) 与 endId(包含) 之间且过期时间小于等于 expiredTime 且 enableRefresh=1 的 token 数据.<br>
     *     用于定时 refreshToken 任务, 不做 spring cache 缓存处理
     * @param expiredTime   过期时间
     * @param startId       起始 id, 包含
     * @param endId         结束 id, 包含
     * @return  符合条件的 {@link AuthTokenPo} 列表
     * @throws Exception   查询错误
     */
    @NonNull
    List<AuthTokenPo> findAuthTokenByExpireTimeAndBetweenId(
            @NonNull Long expiredTime, @NonNull Long startId, @NonNull Long endId) throws Exception;

    /**
     * 根据 tokenId 更新 auth_token 表中的 enableRefresh 字段
     * @param enableRefresh {@link EnableRefresh}
     * @param tokenId       token id
     * @throws Exception    更新异常
     */
    void updateEnableRefreshByTokenId(@NonNull EnableRefresh enableRefresh, @NonNull Long tokenId) throws Exception;
}
