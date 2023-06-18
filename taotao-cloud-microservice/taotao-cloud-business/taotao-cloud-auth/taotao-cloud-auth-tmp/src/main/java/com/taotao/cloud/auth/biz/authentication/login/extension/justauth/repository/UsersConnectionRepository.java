/*
 * Copyright 2002-2018 the original author or authors.
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

package com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository;

import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.entity.ConnectionKey;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository.exception.DuplicateConnectionException;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository.exception.NoSuchConnectionException;
import com.taotao.cloud.auth.biz.authentication.login.extension.justauth.repository.exception.NotConnectedException;
import com.taotao.cloud.security.justauth.justauth.AuthTokenPo;
import com.taotao.cloud.security.justauth.justauth.ConnectionData;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Set;

/**
 * 第三方授权登录用户信息增删改查, 绑定与解绑及查询是否绑定与解绑接口.
 * A data access interface for managing a global store of users connections to service providers.
 * Provides data access operations.
 *
 * @author Keith Donald
 * @author YongWu zheng
 * @version V2.0  Created by 2020-10-08 20:10
 */
@SuppressWarnings({"unused", "UnusedReturnValue"})
public interface UsersConnectionRepository {

	/**
	 * 根据 providerId 与 providerUserId 获取 ConnectionData list.
	 *
	 * @param providerId     第三方服务商, 如: qq, github
	 * @param providerUserId 第三方用户 Id
	 * @return connection data list
	 */
	List<ConnectionData> findConnectionByProviderIdAndProviderUserId(String providerId, String providerUserId);

	/**
	 * Find the ids of the users who are connected to the specific provider user accounts.
	 *
	 * @param providerId      第三方服务商, 如: qq, github
	 * @param providerUserIds the set of provider user ids e.g. ("987665", "435796", "584444").
	 * @return the set of user ids connected to those service provider users, or empty if none.
	 */
	Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds);

	/**
	 * 获取 userId 的所有绑定信息.
	 * Find all connections the current user has across all providers.
	 * The returned map contains an entry for each provider the user is connected to.
	 * The key for each entry is the providerId, and the value is the list of {@link ConnectionData}s that exist between the user and that provider.
	 * For example, if the user is connected once to Facebook and twice to Twitter, the returned map would contain two entries with the following structure:
	 * <pre>
	 * {
	 *     "qq" -&gt; Connection("Jack") ,
	 *     "github"  -&gt; Connection("Tomas"), Connection("Jessica")
	 * }
	 * </pre>
	 * The returned map is sorted by providerId and entry values are ordered by rank.
	 * Returns an empty map if the user has no connections.
	 *
	 * @param userId the userId
	 * @return all connections the current user has across all providers.
	 */
	MultiValueMap<String, ConnectionData> findAllConnections(String userId);

	/**
	 * 获取 userId 和 providerId 的所以绑定信息.
	 * Find the connections the current user has to the provider registered by the given id e.g. 'qq'.
	 * The returned list is ordered by connection rank.
	 * Returns an empty list if the user has no connections to the provider.
	 *
	 * @param userId     本地账户用户 Id
	 * @param providerId the provider id e.g. "qq"
	 * @return the connections the user has to the provider, or an empty list if none
	 */
	List<ConnectionData> findConnections(String userId, String providerId);

	/**
	 * 获取 userId 和 providerUserIds 的所以绑定信息.
	 * Find the connections the current user has to the given provider users.
	 * The providerUsers parameter accepts a map containing an entry for each provider the caller is interested in.
	 * The key for each entry is the providerId e.g. "qq", and the value is a list of provider user ids to fetch
	 * connections to e.g. ("987665", "435796", "584444").
	 * The returned map has the same structure and order, except the provider userId values have been replaced by Connection instances.
	 * If no connection exists between the current user and a given provider user, a null value is returned for that position.
	 *
	 * @param userId          本地账户用户 Id
	 * @param providerUserIds 第三方用户 Id
	 * @return the provider user connection map
	 */
	MultiValueMap<String, ConnectionData> findConnectionsToUsers(String userId, MultiValueMap<String, String> providerUserIds);

	/**
	 * 获取 userId 和 providerId 的所以 rank 值最小的绑定信息.
	 * Get the "primary" connection the current user.
	 * If the user has multiple connections to the provider, this method returns the one with the top rank (or priority).
	 * Useful for direct use by application code to obtain a parameterized Connection instance.
	 *
	 * @param userId     本地账户用户 Id
	 * @param providerId the provider id e.g. "qq"
	 * @return the primary connection
	 * @throws NotConnectedException if the user is not connected to the provider of the API
	 */
	ConnectionData getPrimaryConnection(String userId, String providerId) throws NotConnectedException;

	/**
	 * 绑定.
	 * Add a new connection to this repository for the current user.
	 * After the connection is added, it can be retrieved later using one of the finders defined in this interface.
	 *
	 * @param connection the new connection to add to this repository
	 * @return ConnectionData 这里返回值的目的主要为了更新 spring cache
	 * @throws DuplicateConnectionException if the user already has this connection
	 */
	ConnectionData addConnection(ConnectionData connection);

	/**
	 * Update a Connection already added to this repository.
	 * Merges the field values of the given connection object with the values stored in the repository.
	 *
	 * @param connection the existing connection to update in this repository
	 * @return ConnectionData 这里返回值的目的主要为了更新 spring cache
	 */
	ConnectionData updateConnection(ConnectionData connection);

	/**
	 * 解除绑定.
	 * Remove all Connections between the current user and the provider from this repository.
	 * Does nothing if no provider connections exist.
	 *
	 * @param userId     本地账户用户 Id
	 * @param providerId the provider id e.g. "qq"
	 */
	void removeConnections(String userId, String providerId);

	/**
	 * 解除绑定.
	 * Remove a single Connection for the current user from this repository.
	 * Does nothing if no such connection exists.
	 *
	 * @param userId        本地账户用户 Id
	 * @param connectionKey the connection key
	 */
	void removeConnection(String userId, ConnectionKey connectionKey);

	/**
	 * 根据 userId 与 providerId 获取 ConnectionData. 获取 userId 和 providerId 的所以 rank 值最小的绑定信息.
	 *
	 * @param userId     本地账户用户 Id
	 * @param providerId the provider id e.g. "qq"
	 * @return ConnectionData
	 */
	ConnectionData findPrimaryConnection(String userId, String providerId);

	/**
	 * 根据 userId 和 connectionKey 获取 ConnectionData. 获取 userId 和 providerId, provideUserId 的绑定信息.
	 *
	 * @param userId        本地账户用户 Id
	 * @param connectionKey connectionKey
	 * @return ConnectionData
	 * @throws NoSuchConnectionException no such connection exception
	 */
	ConnectionData getConnection(String userId, ConnectionKey connectionKey) throws NoSuchConnectionException;

	/**
	 * 根据 userId 获取 ConnectionData list. 获取 userId 的所有绑定信息.
	 *
	 * @param userId 本地账户用户 Id
	 * @return connection data list
	 */
	List<ConnectionData> findAllListConnections(String userId);

	/**
	 * 根据 userId 通过指定 providerUsersCriteriaSql 与 parameters 的 sql 获取 ConnectionData list
	 *
	 * @param parameters               sql 的 where 条件 与 对应参数
	 * @param providerUsersCriteriaSql providerUsersCriteriaSql
	 * @param userId                   本地账户用户 Id
	 * @return connection data list
	 */
	List<ConnectionData> findConnectionsToUsers(MapSqlParameterSource parameters, String providerUsersCriteriaSql, String userId);

	/**
	 * 根据 {@code AuthTokenPo#getId()} 更新 {@link ConnectionData}.<br>
	 * 注意: 使用这个接口更新会使 spring cache 缓存更新延迟, 出现缓存强一致性问题, 这接口目的用于 refreshToken 的定时任务, 更新
	 * 时间一般在凌晨 1-5 点.
	 *
	 * @param token {@link AuthTokenPo}
	 * @return 返回更新过的 {@link ConnectionData}, 还可以顺便更新 spring cache
	 */
	ConnectionData updateConnectionByTokenId(AuthTokenPo token);

	/**
	 * 根据 tokenId 查找 {@link ConnectionData}<br>
	 * 注意: 这里不做 spring cache 缓存处理, 这个接口主要用于 refreshToken 的定时任务, 只调用一次, 缓存无意义
	 *
	 * @param tokenId {@code AuthTokenPo#getId()}
	 * @return ConnectionData
	 */
	ConnectionData findConnectionByTokenId(Long tokenId);
}
