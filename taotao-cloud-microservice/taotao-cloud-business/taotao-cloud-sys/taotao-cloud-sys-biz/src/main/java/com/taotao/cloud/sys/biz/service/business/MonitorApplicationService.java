package com.taotao.cloud.sys.biz.service.business;

import com.taotao.boot.cache.redis.repository.RedisRepository;
import com.taotao.cloud.sys.biz.model.vo.monitor.OnlineUserInfo;
import com.taotao.cloud.sys.biz.model.vo.monitor.RedisCacheInfoDTO;
import com.taotao.cloud.sys.biz.model.vo.monitor.ServerInfo;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.connection.RedisServerCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MonitorApplicationService
 *
 * @author shuigedeng
 * @version 2026.03
 * @since 2025-12-19 09:30:45
 */
@Service
@RequiredArgsConstructor
public class MonitorApplicationService {

    @NonNull
    private RedisRepository redisUtil;

    public RedisCacheInfoDTO getRedisCacheInfo() {
        Properties info = (Properties) redisUtil.getRedisTemplate()
                .execute((RedisCallback<Object>) RedisServerCommands::info);
        Properties commandStats = (Properties) redisUtil.getRedisTemplate().execute(
                (RedisCallback<Object>) connection -> connection.info("commandstats"));
        Object dbSize = redisUtil.getRedisTemplate().execute((RedisCallback<Object>) RedisServerCommands::dbSize);

        if (commandStats == null) {
            throw new RuntimeException("找不到对应的redis信息。");
        }

        RedisCacheInfoDTO cacheInfo = new RedisCacheInfoDTO();

        cacheInfo.setInfo(info);
        cacheInfo.setDbSize(dbSize);
        cacheInfo.setCommandStats(new ArrayList<>());

        commandStats.stringPropertyNames().forEach(key -> {
            String property = commandStats.getProperty(key);

            RedisCacheInfoDTO.CommonStatusDTO commonStatus = new RedisCacheInfoDTO.CommonStatusDTO();
            commonStatus.setName(StrUtil.removePrefix(key, "cmdstat_"));
            commonStatus.setValue(StrUtil.subBetween(property, "calls=", ",usec"));

            cacheInfo.getCommandStats().add(commonStatus);
        });

        return cacheInfo;
    }

    public List<OnlineUserInfo> getOnlineUserList( String userName, String ipaddr ) {
//		Collection<String> keys = redisUtil.keys(CacheKeyEnum.LOGIN_USER_KEY.key() + "*");
//
//		Stream<OnlineUserInfo> onlineUserStream = keys.stream().map(o ->
//				CacheCenter.loginUserCache.getObjectOnlyInCacheByKey(o))
//			.filter(Objects::nonNull).map(OnlineUserInfo::new);
//
//		List<OnlineUserInfo> filteredOnlineUsers = onlineUserStream
//			.filter(o ->
//				StrUtil.isEmpty(userName) || userName.equals(o.getUserName())
//			).filter(o ->
//				StrUtil.isEmpty(ipaddr) || ipaddr.equals(o.getIpaddr())
//			).toList();
//
//		Collections.reverse(filteredOnlineUsers);
//		return filteredOnlineUsers;
        return null;
    }

    public ServerInfo getServerInfo() {
        return ServerInfo.fillInfo();
    }


}
