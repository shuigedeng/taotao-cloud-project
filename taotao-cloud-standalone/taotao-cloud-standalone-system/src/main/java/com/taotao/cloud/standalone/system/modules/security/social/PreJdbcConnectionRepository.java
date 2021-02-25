package com.taotao.cloud.standalone.system.modules.security.social;

import com.taotao.cloud.standalone.system.modules.data.tenant.PreTenantContextHolder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.DuplicateConnectionException;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.social.connect.NotConnectedException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @Classname PreConnectionData
 * @Description 个别sql语句问题 需要重写
 * @Author Created by Lihaodong (alias:小东啊) lihaodongmail@163.com
 * @since 2019-07-19 09:18
 * @Version 1.0
 */
@ConditionalOnClass(ConnectionRepository.class)
public class PreJdbcConnectionRepository implements ConnectionRepository {

    private final String userId;
    private final JdbcTemplate jdbcTemplate;
    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final TextEncryptor textEncryptor;
    private final String tablePrefix;
    private final String RANK = "`rank`";

    public PreJdbcConnectionRepository(String userId, JdbcTemplate jdbcTemplate, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor, String tablePrefix) {
        this.userId = userId;
        this.jdbcTemplate = jdbcTemplate;
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.textEncryptor = textEncryptor;
        this.tablePrefix = tablePrefix;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        List<Connection<?>> resultList = jdbcTemplate.query(selectFromUserConnection() + " where userId = ? order by providerId, `rank`", connectionMapper, userId);
        MultiValueMap<String, Connection<?>> connections = new LinkedMultiValueMap<String, Connection<?>>();
        Set<String> registeredProviderIds = connectionFactoryLocator.registeredProviderIds();
        for (String registeredProviderId : registeredProviderIds) {
            connections.put(registeredProviderId, Collections.<Connection<?>>emptyList());
        }
        for (Connection<?> connection : resultList) {
            String providerId = connection.getKey().getProviderId();
            if (connections.get(providerId).size() == 0) {
                connections.put(providerId, new LinkedList<Connection<?>>());
            }
            connections.add(providerId, connection);
        }
        return connections;
    }

    @Override
    public List<Connection<?>> findConnections(String providerId) {
        return jdbcTemplate.query(selectFromUserConnection() + " where userId = ? and providerId = ? order by `rank`", connectionMapper, userId, providerId);
    }

    @Override
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        List<?> connections = findConnections(getProviderId(apiType));
        return (List<Connection<A>>) connections;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(MultiValueMap<String, String> providerUsers) {
        if (providerUsers == null || providerUsers.isEmpty()) {
            throw new IllegalArgumentException("Unable to execute find: no providerUsers provided");
        }
        StringBuilder providerUsersCriteriaSql = new StringBuilder();
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("userId", userId);
        for (Iterator<Entry<String, List<String>>> it = providerUsers.entrySet().iterator(); it.hasNext(); ) {
            Entry<String, List<String>> entry = it.next();
            String providerId = entry.getKey();
            providerUsersCriteriaSql.append("providerId = :providerId_").append(providerId).append(" and providerUserId in (:providerUserIds_").append(providerId).append(")");
            parameters.addValue("providerId_" + providerId, providerId);
            parameters.addValue("providerUserIds_" + providerId, entry.getValue());
            if (it.hasNext()) {
                providerUsersCriteriaSql.append(" or ");
            }
        }
        List<Connection<?>> resultList = new NamedParameterJdbcTemplate(jdbcTemplate).query(selectFromUserConnection() + " where userId = :userId and " + providerUsersCriteriaSql + " order by providerId, `rank`", parameters, connectionMapper);
        MultiValueMap<String, Connection<?>> connectionsForUsers = new LinkedMultiValueMap<String, Connection<?>>();
        for (Connection<?> connection : resultList) {
            String providerId = connection.getKey().getProviderId();
            List<String> userIds = providerUsers.get(providerId);
            List<Connection<?>> connections = connectionsForUsers.get(providerId);
            if (connections == null) {
                connections = new ArrayList<Connection<?>>(userIds.size());
                for (int i = 0; i < userIds.size(); i++) {
                    connections.add(null);
                }
                connectionsForUsers.put(providerId, connections);
            }
            String providerUserId = connection.getKey().getProviderUserId();
            int connectionIndex = userIds.indexOf(providerUserId);
            connections.set(connectionIndex, connection);
        }
        return connectionsForUsers;
    }

    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey) {
        try {
            return jdbcTemplate.queryForObject(selectFromUserConnection() + " where userId = ? and providerId = ? and providerUserId = ?", connectionMapper, userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
        } catch (EmptyResultDataAccessException e) {
            throw new NoSuchConnectionException(connectionKey);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) getConnection(new ConnectionKey(providerId, providerUserId));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        Connection<A> connection = (Connection<A>) findPrimaryConnection(providerId);
        if (connection == null) {
            throw new NotConnectedException(providerId);
        }
        return connection;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        String providerId = getProviderId(apiType);
        return (Connection<A>) findPrimaryConnection(providerId);
    }

    /**
     * 添加时需要带上租户id
     * @param connection
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void addConnection(Connection<?> connection) {
        try {
            // 获取租户id
            Long tenantId = PreTenantContextHolder.getCurrentTenantId();
            ConnectionData data = connection.createData();
            int rank = jdbcTemplate.queryForObject("select coalesce(max(" + RANK + ") + 1, 1) as ranks from " + tablePrefix + "UserConnection where userId = ? and providerId = ? and tenant_id = ?", new Object[]{userId, data.getProviderId(),tenantId}, Integer.class);
            jdbcTemplate.update("insert into " + tablePrefix + "UserConnection (userId, providerId, providerUserId, `rank`, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime,create_time,tenant_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    userId, data.getProviderId(), data.getProviderUserId(), rank, data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), encrypt(data.getAccessToken()), encrypt(data.getSecret()), encrypt(data.getRefreshToken()), data.getExpireTime(), new Timestamp(System.currentTimeMillis()),tenantId);
        } catch (DuplicateKeyException e) {
            throw new DuplicateConnectionException(connection.getKey());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateConnection(Connection<?> connection) {
        ConnectionData data = connection.createData();
        jdbcTemplate.update("update " + tablePrefix + "UserConnection set displayName = ?, profileUrl = ?, imageUrl = ?, accessToken = ?, secret = ?, refreshToken = ?, expireTime = ? where userId = ? and providerId = ? and providerUserId = ?",
                data.getDisplayName(), data.getProfileUrl(), data.getImageUrl(), encrypt(data.getAccessToken()), encrypt(data.getSecret()), encrypt(data.getRefreshToken()), data.getExpireTime(), userId, data.getProviderId(), data.getProviderUserId());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeConnections(String providerId) {
        jdbcTemplate.update("delete from " + tablePrefix + "UserConnection where userId = ? and providerId = ?", userId, providerId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeConnection(ConnectionKey connectionKey) {
        jdbcTemplate.update("delete from " + tablePrefix + "UserConnection where userId = ? and providerId = ? and providerUserId = ?", userId, connectionKey.getProviderId(), connectionKey.getProviderUserId());
    }

    // internal helpers

    private String selectFromUserConnection() {
        return "select userId, providerId, providerUserId, displayName, profileUrl, imageUrl, accessToken, secret, refreshToken, expireTime from " + tablePrefix + "UserConnection";
    }

    private Connection<?> findPrimaryConnection(String providerId) {
        List<Connection<?>> connections = jdbcTemplate.query(selectFromUserConnection() + " where userId = ? and providerId = ? order by `rank`", connectionMapper, userId, providerId);
        if (connections.size() > 0) {
            return connections.get(0);
        } else {
            return null;
        }
    }

    private final ServiceProviderConnectionMapper connectionMapper = new ServiceProviderConnectionMapper();

    private final class ServiceProviderConnectionMapper implements RowMapper<Connection<?>> {
        @Override
        public Connection<?> mapRow(ResultSet rs, int rowNum) throws SQLException {
            ConnectionData connectionData = mapConnectionData(rs);
            ConnectionFactory<?> connectionFactory = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId());
            return connectionFactory.createConnection(connectionData);
        }

        private ConnectionData mapConnectionData(ResultSet rs) throws SQLException {
            return new ConnectionData(rs.getString("providerId"), rs.getString("providerUserId"), rs.getString("displayName"), rs.getString("profileUrl"), rs.getString("imageUrl"),
                    decrypt(rs.getString("accessToken")), decrypt(rs.getString("secret")), decrypt(rs.getString("refreshToken")), expireTime(rs.getLong("expireTime")));
        }

        private String decrypt(String encryptedText) {
            return encryptedText != null ? textEncryptor.decrypt(encryptedText) : encryptedText;
        }

        private Long expireTime(long expireTime) {
            return expireTime == 0 ? null : expireTime;
        }

    }

    private <A> String getProviderId(Class<A> apiType) {
        return connectionFactoryLocator.getConnectionFactory(apiType).getProviderId();
    }

    private String encrypt(String text) {
        return text != null ? textEncryptor.encrypt(text) : text;
    }

}
