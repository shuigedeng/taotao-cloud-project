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

package com.taotao.cloud.auth.application.login.extension.justauth.repository.jdbc;

import com.taotao.cloud.auth.application.login.extension.justauth.JustAuthRequestHolder;
import com.taotao.cloud.auth.application.login.extension.justauth.entity.ConnectionKey;
import com.taotao.cloud.auth.application.login.extension.justauth.properties.RepositoryProperties;
import com.taotao.cloud.auth.application.login.extension.justauth.repository.UsersConnectionRepository;
import com.taotao.cloud.auth.application.login.extension.justauth.repository.exception.DuplicateConnectionException;
import com.taotao.cloud.auth.application.login.extension.justauth.repository.exception.NoSuchConnectionException;
import com.taotao.cloud.auth.application.login.extension.justauth.repository.exception.NotConnectedException;
import com.taotao.cloud.security.justauth.justauth.AuthTokenPo;
import com.taotao.cloud.security.justauth.justauth.ConnectionData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * A data access interface for managing a global store of users connections to service providers.
 * Provides data access operations.
 * 抽取了 sql 语句，与 用户表的字段名称到 {@link RepositoryProperties},
 * 更便于用户自定义。<br><br>
 * redis 缓存 key 说明: "h:"(单个返回值)/"hs(多个返回值):" 前缀表示为 hash key 前缀(绝对匹配), "hm:" 前缀表示为 hash key 前缀(父 key 可匹配, field 不能),
 * "__" 为 hash key 的分隔符. 下面为 key 列表:
 * <pre>
 * USER_CONNECTION_HASH_CACHE_NAME:    'hs:' + providerId + '__' + providerUserId
 * USER_CONNECTION_HASH_CACHE_NAME:    'h:' + userId + ':' + providerId + '__' + providerUserId
 * USER_CONNECTION_HASH_CACHE_NAME:    'h:' + userId + '__' + providerId
 * USER_CONNECTION_HASH_CACHE_NAME:    'hs:' + userId + '__' + providerId
 *
 * USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME:   'hm:' + providerId + '__' + providerUserIds
 * USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME:   'hm:' + userId + '__' + methodName(findAllConnections,findAllListConnections)
 * USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME:   'hm:' + userId + '__' + providerUsers
 * USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME:   'hm:' + userId + '__' + parameters
 * </pre>
 *
 * @author YongWu zheng
 * @version V2.0  Created by 2020/5/13 13:41
 */
@Slf4j
// @CacheConfig(cacheManager = "auth2RedisHashCacheManager")
public class Auth2JdbcUsersConnectionRepository implements UsersConnectionRepository {

    private final JdbcTemplate jdbcTemplate;

    private final TextEncryptor textEncryptor;

    private final RepositoryProperties repositoryProperties;

    public Auth2JdbcUsersConnectionRepository(
            JdbcTemplate auth2UserConnectionJdbcTemplate,
            TextEncryptor textEncryptor,
            RepositoryProperties repositoryProperties) {
        this.jdbcTemplate = auth2UserConnectionJdbcTemplate;
        this.textEncryptor = textEncryptor;
        this.repositoryProperties = repositoryProperties;
    }

    //	@Cacheable(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //		key = "'hs:' + #providerId + '__' + #providerUserId")
    @Override
    public List<ConnectionData> findConnectionByProviderIdAndProviderUserId(String providerId, String providerUserId) {
        try {
            return jdbcTemplate.query(
                    String.format(
                            "%s WHERE %s = ? AND %s = ? ORDER BY %s",
                            repositoryProperties.getSelectFromUserConnectionSql(),
                            repositoryProperties.getProviderIdColumnName(),
                            repositoryProperties.getProviderUserIdColumnName(),
                            repositoryProperties.getRankColumnName()),
                    connectionDataMapper,
                    providerId,
                    providerUserId);
        } catch (Exception e) {
            String msg = String.format(
                    "findConnectionByProviderIdAndProviderUserId, providerId=%s, providerUserId=%s. sql query error: %s",
                    providerId, providerUserId, e.getMessage());
            log.error(msg, e);
            return null;
        }
    }

    //	@Cacheable(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //		key = "'hm:' + #providerId + '__' + #providerUserIds")
    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(repositoryProperties.getProviderIdColumnName(), providerId);
        parameters.addValue(repositoryProperties.getProviderUserIdColumnName(), providerUserIds);
        final Set<String> localUserIds = new HashSet<>();
        try {
            new NamedParameterJdbcTemplate(jdbcTemplate)
                    .query(repositoryProperties.getFindUserIdsConnectedToSql(), parameters, rs -> {
                        while (rs.next()) {
                            localUserIds.add(rs.getString(repositoryProperties.getUserIdColumnName()));
                        }
                        return localUserIds;
                    });
        } catch (Exception e) {
            String msg = String.format(
                    "findUserIdsConnectedTo: providerId=%s, providerUserIds=%s. sql query error: %s",
                    providerId, providerUserIds, e.getMessage());
            log.error(msg, e);
        }
        return localUserIds;
    }

    @Override
    //	@Cacheable(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME, key = "'hm:' +
    // #userId + '__' + #root.methodName")
    public MultiValueMap<String, ConnectionData> findAllConnections(String userId) {
        return getConnectionMap(findAllListConnections(userId));
    }

    @Override
    //	@Cacheable(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //		key = "'hs:' + #userId + '__' + #providerId")
    public List<ConnectionData> findConnections(String userId, String providerId) {
        return getConnectionDataList(userId, providerId);
    }

    @Override
    //	@Cacheable(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //		key = "'hm:' + #userId + '__' + #providerUsers")
    public MultiValueMap<String, ConnectionData> findConnectionsToUsers(
            String userId, MultiValueMap<String, String> providerUsers) {
        if (providerUsers == null || providerUsers.isEmpty()) {
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }
        StringBuilder providerUsersCriteriaSql = new StringBuilder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue(repositoryProperties.getUserIdColumnName(), userId);
        for (Iterator<Map.Entry<String, List<String>>> it =
                        providerUsers.entrySet().iterator();
                it.hasNext(); ) {
            Map.Entry<String, List<String>> entry = it.next();
            fillingCriteriaSql(providerUsersCriteriaSql, parameters, it, entry);
        }
        return getConnectionMap(
                findConnectionsToUsers(parameters, providerUsersCriteriaSql.toString(), userId), providerUsers);
    }

    @Override
    //	@Cacheable(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //		key = "'h:' + #userId + ':' + #connectionKey.providerId + '__' + #connectionKey.providerUserId")
    public ConnectionData getConnection(String userId, ConnectionKey connectionKey) throws NoSuchConnectionException {

        try {
            return jdbcTemplate.queryForObject(
                    String.format(
                            "%s where %s = ? and %s = ? and %s = ?",
                            repositoryProperties.getSelectFromUserConnectionSql(),
                            repositoryProperties.getUserIdColumnName(),
                            repositoryProperties.getProviderIdColumnName(),
                            repositoryProperties.getProviderUserIdColumnName()),
                    connectionDataMapper,
                    userId,
                    connectionKey.getProviderId(),
                    connectionKey.getProviderUserId());
        } catch (Exception e) {
            String msg = String.format(
                    "getConnection: userId=%s, connectionKey=%s. sql query error: %s",
                    userId, connectionKey, e.getMessage());
            log.error(msg);
            throw new NoSuchConnectionException(connectionKey);
        }
    }

    @Override
    //	@Cacheable(cacheNames = USER_CONNECTION_HASH_CACHE_NAME, key = "'h:' + #userId + '__' + #providerId")
    public ConnectionData findPrimaryConnection(String userId, String providerId) {
        List<ConnectionData> connectionDataList = getConnectionDataList(userId, providerId);
        if (connectionDataList != null && connectionDataList.size() > 0) {
            return connectionDataList.get(0);
        } else {
            return null;
        }
    }

    @Override
    //	@Cacheable(cacheNames = USER_CONNECTION_HASH_CACHE_NAME, key = "'h:' + #userId + '__' + #providerId")
    public ConnectionData getPrimaryConnection(String userId, String providerId) throws NotConnectedException {
        ConnectionData connection = findPrimaryConnection(userId, providerId);
        if (connection == null) {
            throw new NotConnectedException(userId + ":" + providerId);
        }
        return connection;
    }

    private List<ConnectionData> getConnectionDataList(String userId, String providerId) {
        try {
            return jdbcTemplate.query(
                    String.format(
                            "%s where %s = ? and %s = ? order by %s",
                            repositoryProperties.getSelectFromUserConnectionSql(),
                            repositoryProperties.getUserIdColumnName(),
                            repositoryProperties.getProviderIdColumnName(),
                            repositoryProperties.getRankColumnName()),
                    connectionDataMapper,
                    userId,
                    providerId);
        } catch (Exception e) {
            String msg = String.format(
                    "getConnectionDataList: userId=%s, providerId=%s. sql query error: %s",
                    userId, providerId, e.getMessage());
            log.error(msg, e);
            return null;
        }
    }

    //	@Caching(
    //		evict = {@CacheEvict(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //			key = "'hm:' + #connection.userId", beforeInvocation = true),
    //			@CacheEvict(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //				key = "'hm:' + #connection.providerId", beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'h:' + #connection.userId + ':' + #connection.providerId + '__' + #connection.providerUserId",
    //				beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'h:' + #connection.userId + '__' + #connection.providerId",
    //				beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'hs:' + #connection.userId + '__' + #connection.providerId",
    //				beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'hs:' + #connection.providerId + '__' + #connection.providerUserId",
    //				beforeInvocation = true)
    //		},
    //		put = {@CachePut(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //			// 假定一个本地用户只能绑定一个同一第三方账号
    //			key = "'h:' + #connection.userId + '__' + #connection.providerId"),
    //			@CachePut(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'h:' + #connection.userId + ':' + #connection.providerId + '__' " +
    //					"+ #connection.providerUserId")
    //		}
    //	)
    @Override
    @Transactional(
            rollbackFor = {Exception.class},
            propagation = Propagation.REQUIRED)
    public ConnectionData addConnection(ConnectionData connection) {
        addConnectionData(connection);
        return connection;
    }

    private void addConnectionData(ConnectionData connection) {
        try {
            //noinspection ConstantConditions
            int rank = jdbcTemplate.queryForObject(
                    repositoryProperties.getAddConnectionQueryForRankSql(),
                    Integer.class,
                    connection.getUserId(),
                    connection.getProviderId());
            jdbcTemplate.update(
                    repositoryProperties.getAddConnectionSql(),
                    connection.getUserId(),
                    connection.getProviderId(),
                    connection.getProviderUserId(),
                    rank,
                    connection.getDisplayName(),
                    connection.getProfileUrl(),
                    connection.getImageUrl(),
                    encrypt(connection.getAccessToken()),
                    connection.getTokenId(),
                    encrypt(connection.getRefreshToken()),
                    connection.getExpireTime());
        } catch (DuplicateKeyException e) {
            throw new DuplicateConnectionException(
                    new ConnectionKey(connection.getProviderId(), connection.getProviderUserId()));
        }
    }

    //	@Caching(
    //		evict = {@CacheEvict(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //			key = "'hm:' + #connection.userId", beforeInvocation = true),
    //			@CacheEvict(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //				key = "'hm:' + #connection.providerId", beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'h:' + #connection.userId + ':' + #connection.providerId + '__' + #connection.providerUserId",
    //				beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'h:' + #connection.userId + '__' + #connection.providerId",
    //				beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'hs:' + #connection.userId + '__' + #connection.providerId",
    //				beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'hs:' + #connection.providerId + '__' + #connection.providerUserId",
    //				beforeInvocation = true)
    //		},
    //		put = {@CachePut(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //			// 假定一个本地用户只能绑定一个同一第三方账号
    //			key = "'h:' + #connection.userId + '__' + #connection.providerId"),
    //			@CachePut(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'h:' + #connection.userId + ':' + #connection.providerId + '__' " +
    //					"+ #connection.providerUserId")
    //		}
    //	)
    @Override
    @Transactional(
            rollbackFor = {Exception.class},
            propagation = Propagation.REQUIRED)
    public ConnectionData updateConnection(ConnectionData connection) {
        jdbcTemplate.update(
                repositoryProperties.getUpdateConnectionSql(),
                connection.getDisplayName(),
                connection.getProfileUrl(),
                connection.getImageUrl(),
                encrypt(connection.getAccessToken()),
                connection.getTokenId(),
                encrypt(connection.getRefreshToken()),
                connection.getExpireTime(),
                connection.getUserId(),
                connection.getProviderId(),
                connection.getProviderUserId());
        return connection;
    }

    //	@Caching(
    //		evict = {@CacheEvict(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //			key = "'hm:' + #result.userId"),
    //			@CacheEvict(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //				key = "'hm:' + #result.providerId"),
    //			@CacheEvict(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'hs:' + #result.userId + '__' + #result.providerId"),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'hs:' + #result.providerId + '__' + #result.providerUserId")
    //		},
    //		put = {@CachePut(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //			// 假定一个本地用户只能绑定一个同一第三方账号
    //			key = "'h:' + #result.userId + '__' + #result.providerId"),
    //			@CachePut(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'h:' + #result.userId + ':' + #result.providerId + '__' " +
    //					"+ #result.providerUserId")
    //		}
    //	)
    @Override
    @Transactional(
            rollbackFor = {Exception.class},
            propagation = Propagation.REQUIRED)
    public ConnectionData updateConnectionByTokenId(AuthTokenPo token) {
        ConnectionData connection = findConnectionByTokenId(token.getId());
        connection.setAccessToken(token.getAccessToken());
        connection.setRefreshToken(token.getRefreshToken());
        connection.setExpireTime(token.getExpireTime());
        updateConnection(connection);
        return connection;
    }

    @Override
    public ConnectionData findConnectionByTokenId(Long tokenId) {
        return jdbcTemplate.queryForObject(
                String.format(
                        "%s where %s = ?",
                        repositoryProperties.getSelectFromUserConnectionSql(),
                        repositoryProperties.getTokenIdColumnName()),
                connectionDataMapper,
                tokenId);
    }

    //	@Caching(
    //		evict = {@CacheEvict(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //			key = "'hm:' + #userId", beforeInvocation = true),
    //			@CacheEvict(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //				key = "'hm:' + #providerId", beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'h:' + #userId + ':' + #providerId", beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'h:' + #userId + '__' + #providerId", beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'hs:' + #userId + ':' + #providerId", beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'hs:' + #providerId", beforeInvocation = true)
    //		}
    //	)
    @Override
    @Transactional(
            rollbackFor = {Exception.class},
            propagation = Propagation.REQUIRED)
    public void removeConnections(String userId, String providerId) {
        jdbcTemplate.update(repositoryProperties.getRemoveConnectionsSql(), userId, providerId);
    }

    //	@Caching(
    //		evict = {@CacheEvict(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //			key = "'hm:' + #userId", beforeInvocation = true),
    //			@CacheEvict(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME,
    //				key = "'hm:' + #connectionKey.providerId", beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				// key = "'h:' + #userId + ':' + #connectionKey.providerId + '__' + #connectionKey.providerUserId"
    //				keyGenerator = "removeConnectionsByConnectionKeyWithUserIdKeyGenerator",
    //				beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'h:' + #userId + '__' + #connectionKey.providerId",
    //				beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'hs:' + #userId + '__' + #connectionKey.providerId",
    //				beforeInvocation = true),
    //			@CacheEvict(cacheNames = USER_CONNECTION_HASH_CACHE_NAME,
    //				key = "'hs:' + #connectionKey.providerId + '__' + #connectionKey.providerUserId",
    //				beforeInvocation = true)
    //		}
    //	)
    @Override
    @Transactional(
            rollbackFor = {Exception.class},
            propagation = Propagation.REQUIRED)
    public void removeConnection(String userId, ConnectionKey connectionKey) {
        jdbcTemplate.update(
                repositoryProperties.getRemoveConnectionSql(),
                userId,
                connectionKey.getProviderId(),
                connectionKey.getProviderUserId());
    }

    @Override
    //	@Cacheable(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME, key = "'hm:' +
    // #userId + '__' + #root.methodName")
    public List<ConnectionData> findAllListConnections(String userId) {
        try {
            return jdbcTemplate.query(
                    String.format(
                            "%s where %s = ? order by %s, %s",
                            repositoryProperties.getSelectFromUserConnectionSql(),
                            repositoryProperties.getUserIdColumnName(),
                            repositoryProperties.getProviderIdColumnName(),
                            repositoryProperties.getRankColumnName()),
                    connectionDataMapper,
                    userId);
        } catch (Exception e) {
            String msg =
                    String.format("findAllListConnections: userId=%s. sql query error: %s", userId, e.getMessage());
            log.error(msg, e);
            return null;
        }
    }

    @Override
    //	@Cacheable(cacheNames = RedisCacheAutoConfiguration.USER_CONNECTION_HASH_ALL_CLEAR_CACHE_NAME, key = "'hm:' +
    // #userId + '__' + #parameters")
    public List<ConnectionData> findConnectionsToUsers(
            MapSqlParameterSource parameters, String providerUsersCriteriaSql, String userId) {
        try {
            return new NamedParameterJdbcTemplate(jdbcTemplate)
                    .query(
                            String.format(
                                    "%s where %s = :userId and %s order by %s, %s",
                                    repositoryProperties.getSelectFromUserConnectionSql(),
                                    repositoryProperties.getUserIdColumnName(),
                                    providerUsersCriteriaSql,
                                    repositoryProperties.getProviderIdColumnName(),
                                    repositoryProperties.getRankColumnName()),
                            parameters,
                            connectionDataMapper);
        } catch (Exception e) {
            String msg = String.format(
                    "findConnectionsToUsers: userId=%s, parameters=%s, providerUsersCriteriaSql=%s. sql query error: %s",
                    userId, parameters, providerUsersCriteriaSql, e.getMessage());
            log.error(msg, e);
            return null;
        }
    }

    private MultiValueMap<String, ConnectionData> getConnectionMap(List<ConnectionData> connectionList) {
        MultiValueMap<String, ConnectionData> connections = new LinkedMultiValueMap<>();

        Collection<String> registeredProviderIds = JustAuthRequestHolder.getValidProviderIds();
        for (String registeredProviderId : registeredProviderIds) {
            connections.put(registeredProviderId, Collections.emptyList());
        }
        for (ConnectionData connection : connectionList) {
            String providerId = connection.getProviderId();
            List<ConnectionData> list = connections.get(providerId);
            if (CollectionUtils.isEmpty(list)) {
                connections.put(providerId, new LinkedList<>());
            }
            connections.add(providerId, connection);
        }
        return connections;
    }

    private MultiValueMap<String, ConnectionData> getConnectionMap(
            List<ConnectionData> connectionList, MultiValueMap<String, String> providerUsers) {
        MultiValueMap<String, ConnectionData> connectionsForUsers = new LinkedMultiValueMap<>();
        for (ConnectionData connection : connectionList) {
            String providerId = connection.getProviderId();
            List<String> userIds = providerUsers.get(providerId);
            List<ConnectionData> connections = connectionsForUsers.get(providerId);
            if (connections == null) {
                connections = new ArrayList<>(userIds.size());
                for (int i = 0; i < userIds.size(); i++) {
                    connections.add(null);
                }
                connectionsForUsers.put(providerId, connections);
            }
            String providerUserId = connection.getProviderUserId();
            int connectionIndex = userIds.indexOf(providerUserId);
            connections.set(connectionIndex, connection);
        }
        return connectionsForUsers;
    }

    private String encrypt(String text) {
        return text != null ? textEncryptor.encrypt(text) : null;
    }

    private void fillingCriteriaSql(
            StringBuilder providerUsersCriteriaSql,
            MapSqlParameterSource parameters,
            Iterator<Map.Entry<String, List<String>>> it,
            Map.Entry<String, List<String>> entry) {
        String providerId = entry.getKey();
        providerUsersCriteriaSql
                .append(String.format("%s = :providerId_", repositoryProperties.getProviderIdColumnName()))
                .append(providerId)
                .append(String.format(
                        " and %s in (:providerUserIds_", repositoryProperties.getProviderUserIdColumnName()))
                .append(providerId)
                .append(")");
        parameters.addValue(
                String.format("%s_%s", repositoryProperties.getProviderIdColumnName(), providerId), providerId);
        parameters.addValue(
                String.format("%s_%s", repositoryProperties.getProviderUserIdColumnName(), providerId),
                entry.getValue());
        if (it.hasNext()) {
            providerUsersCriteriaSql.append(" or ");
        }
    }

    private final ServiceProviderConnectionDataMapper connectionDataMapper = new ServiceProviderConnectionDataMapper();

    private final class ServiceProviderConnectionDataMapper implements RowMapper<ConnectionData> {

        @Override
        public ConnectionData mapRow(@NonNull ResultSet rs, int rowNum) throws SQLException {
            return mapConnectionData(rs);
        }

        private ConnectionData mapConnectionData(ResultSet rs) throws SQLException {
            ConnectionData connectionData = new ConnectionData();
            connectionData.setUserId(rs.getString(repositoryProperties.getUserIdColumnName()));
            connectionData.setProviderId(rs.getString(repositoryProperties.getProviderIdColumnName()));
            connectionData.setProviderUserId(rs.getString(repositoryProperties.getProviderUserIdColumnName()));
            connectionData.setDisplayName(rs.getString(repositoryProperties.getDisplayNameColumnName()));
            connectionData.setProfileUrl(rs.getString(repositoryProperties.getProfileUrlColumnName()));
            connectionData.setImageUrl(rs.getString(repositoryProperties.getImageUrlColumnName()));
            connectionData.setAccessToken(decrypt(rs.getString(repositoryProperties.getAccessTokenColumnName())));
            connectionData.setTokenId(rs.getLong(repositoryProperties.getTokenIdColumnName()));
            connectionData.setRefreshToken(decrypt(rs.getString(repositoryProperties.getRefreshTokenColumnName())));
            connectionData.setExpireTime(expireTime(rs.getLong(repositoryProperties.getExpireTimeColumnName())));
            return connectionData;
        }

        private String decrypt(String encryptedText) {
            return encryptedText != null ? textEncryptor.decrypt(encryptedText) : null;
        }

        private Long expireTime(long expireTime) {
            return expireTime == 0 ? null : expireTime;
        }
    }
}
