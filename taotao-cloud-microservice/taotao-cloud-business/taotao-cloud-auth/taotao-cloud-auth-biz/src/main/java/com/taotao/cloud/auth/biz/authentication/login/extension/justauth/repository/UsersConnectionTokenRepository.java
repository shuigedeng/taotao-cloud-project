/*
 * MIT License
 * Copyright (c) 2020-2029 YongWu zheng (dcenter.top and gitee.com/pcore and github.com/ZeroOrInfinity)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository;

import com.taotao.cloud.security.justauth.justauth.AuthTokenPo;
import com.taotao.cloud.security.justauth.justauth.EnableRefresh;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.List;

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
	List<AuthTokenPo> findAuthTokenByExpireTimeAndBetweenId(@NonNull Long expiredTime, @NonNull Long startId,
	                                                        @NonNull Long endId) throws Exception;

	/**
	 * 根据 tokenId 更新 auth_token 表中的 enableRefresh 字段
	 * @param enableRefresh {@link EnableRefresh}
	 * @param tokenId       token id
	 * @throws Exception    更新异常
	 */
	void updateEnableRefreshByTokenId(@NonNull EnableRefresh enableRefresh, @NonNull Long tokenId) throws Exception;


}
