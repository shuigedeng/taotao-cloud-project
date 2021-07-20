package com.taotao.cloud.standalone.system.modules.security.social;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @Classname SocialRedisHelper
 * @Description 将第三方用户信息保存到redis里面
 * @Author shuigedeng
 * @since 2019-07-06 11:48
 * @Version 1.0
 */
@Component
public class SocialRedisHelper {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;

    /**
     * 缓存第三方用户信息
     * @param mkey
     * @param connectionData
     */
    public void saveConnectionData(String mkey, PreConnectionData connectionData) {
        redisTemplate.opsForValue().set(getKey(mkey), connectionData, 10, TimeUnit.MINUTES);
    }

    /**
     *
     * @param mkey
     * @param userId
     */
    public void saveStateUserId(String mkey, String userId) {
        redisTemplate.opsForValue().set(getKey(mkey), userId, 10, TimeUnit.MINUTES);
    }

    /**
     *
     * @param mkey
     * @return
     */
    public String getStateUserId(String mkey) {
        String key = getKey(mkey);
        if (!redisTemplate.hasKey(key)) {
            throw new RuntimeException("无法找到缓存的第三方社交账号信息");
        }
        return (String) redisTemplate.opsForValue().get(key);
    }


    /**
     * 第三方社交账号信息进行与业务系统账号绑定
     * @param mkey
     * @param userId
     */
    public void doPostSignUp(String mkey, Integer userId) {
        String key = getKey(mkey);
        if (!redisTemplate.hasKey(key)) {
            throw new RuntimeException("无法找到缓存的第三方社交账号信息");
        }
        PreConnectionData preConnectionData = (PreConnectionData) redisTemplate.opsForValue().get(key);
        ConnectionData connectionData = null;
        if (preConnectionData != null) {
            connectionData = new ConnectionData(preConnectionData.getProviderId(),preConnectionData.getProviderUserId(),preConnectionData.getDisplayName(),preConnectionData.getProfileUrl(),preConnectionData.getImageUrl(),preConnectionData.getAccessToken(),preConnectionData.getSecret(),preConnectionData.getRefreshToken(),preConnectionData.getExpireTime());
        }
        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(preConnectionData.getProviderId())
                .createConnection(connectionData);
        usersConnectionRepository.createConnectionRepository(String.valueOf(userId)).addConnection(connection);
        redisTemplate.delete(key);
    }

    /**
     * 第三方社交账号信息进行与业务系统账号解绑
     * @param userId
     */
    public void doPostSignDown(Integer userId,String providerId) {
        usersConnectionRepository.createConnectionRepository(String.valueOf(userId)).removeConnections(providerId);
    }


    private String getKey(String key) {
        if (StringUtils.isEmpty(key)) {
            throw new RuntimeException("key不为空");
        }
        return "pre:security:social.connect." + key;
    }
}
