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

package com.taotao.cloud.workflow.biz.common.util;

import com.taotao.cloud.workflow.biz.common.database.data.DataSourceContextHolder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/** */
@Slf4j
@Component
public class RedisUtil {
    /** 默认缓存时间 60S */
    public static final int CAHCETIME = 60;
    /** 默认缓存时间 1hr */
    public static final int CAHCEHOUR = 60 * 60;
    /** 默认缓存时间 1Day */
    public static final int CAHCEDAY = 60 * 60 * 24;
    /** 默认缓存时间 1week */
    public static final int CAHCEWEEK = 60 * 60 * 24 * 7;
    /** 默认缓存时间 1month */
    public static final int CAHCEMONTH = 60 * 60 * 24 * 7 * 30;
    /** 默认缓存时间 1年 */
    public static final int CAHCEYEAR = 60 * 60 * 24 * 7 * 30 * 12;

    private static long expiresIn = TimeUnit.SECONDS.toSeconds(CAHCEHOUR * 8);

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private CacheKeyUtil cacheKeyUtil;
    // =============================common============================

    /**
     * 获取所有的key
     *
     * @return
     */
    public Set<String> getAllKeys() {
        String tenantId = DataSourceContextHolder.getDatasourceId();
        Set<String> keys = Collections.emptySet();
        if (tenantId == null) {
            keys = redisTemplate.keys("*");
        } else {
            keys = redisTemplate.keys("*" + tenantId + "*");
        }
        if (CollectionUtils.isNotEmpty(keys)) {
            // 排除ID生成器的缓存记录， 如果删除在集群情况下ID生成器的机器编号可能会重复导致生成的ID重复
            keys = keys.stream()
                    .filter(s -> !s.startsWith(CacheKeyUtil.IDGENERATOR))
                    .collect(Collectors.toSet());
        }
        return keys;
    }

    /**
     * 获取所有的key
     *
     * @return
     */
    public Set<String> getAllVisiualKeys() {
        Set<String> allKey = new HashSet<>(16);
        allKey.addAll(Objects.requireNonNull(redisTemplate.keys("*" + cacheKeyUtil.getAllUser() + "*")));
        allKey.addAll(Objects.requireNonNull(redisTemplate.keys("*" + cacheKeyUtil.getCompanySelect() + "*")));
        allKey.addAll(Objects.requireNonNull(redisTemplate.keys("*" + cacheKeyUtil.getDictionary() + "*")));
        allKey.addAll(Objects.requireNonNull(redisTemplate.keys("*" + cacheKeyUtil.getDynamic() + "*")));
        allKey.addAll(Objects.requireNonNull(redisTemplate.keys("*" + cacheKeyUtil.getOrganizeList() + "*")));
        allKey.addAll(Objects.requireNonNull(redisTemplate.keys("*" + cacheKeyUtil.getPositionList() + "*")));
        allKey.addAll(Objects.requireNonNull(redisTemplate.keys("*" + cacheKeyUtil.getVisiualData() + "*")));
        return allKey;
    }

    /**
     * 返回 key 的剩余的过期时间
     *
     * @param key
     * @return
     */
    public Long getLiveTime(String key) {
        return redisTemplate.getExpire(key);
    }

    /**
     * 删除指定key
     *
     * @param key
     */
    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /** 删除可视化redis */
    public void removeSome(String key) {
        Set<String> keys = redisTemplate.keys("*" + CacheKeyUtil.VISIUALDATA + key + "*");
        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    /** 删除用户对应的token,redis */
    public void removeUserToken(String userToken) {
        Set<String> keys = redisTemplate.keys(userToken + "*");
        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    /** 删除全部redis */
    public void removeAll() {
        Set<String> keys = getAllKeys();
        if (CollectionUtils.isNotEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 指定缓存失效时间
     *
     * @param key
     * @param time
     * @return
     */
    public void expire(String key, long time) {
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        } else {
            redisTemplate.expire(key, expiresIn, TimeUnit.SECONDS);
        }
    }

    /**
     * 插入缓存(无时间)
     *
     * @param key
     * @param object
     */
    public void insert(String key, Object object) {
        insert(key, object, 0);
    }

    /**
     * 插入缓存(有时间)
     *
     * @param key
     * @param object
     */
    public void insert(String key, Object object, long time) {
        if (object instanceof Map) {
            redisTemplate.opsForHash().putAll(key, (Map<String, String>) object);
        } else if (object instanceof List) {
            redisTemplate.opsForList().rightPush(key, object);
        } else if (object instanceof Set) {
            redisTemplate.opsForSet().add(key, ((Set<?>) object).toArray());
        } else {
            redisTemplate.opsForValue().set(key, toJson(object));
        }
        expire(key, time);
    }

    /** Object转成JSON数据 */
    private String toJson(Object object) {
        if (object instanceof Integer
                || object instanceof Long
                || object instanceof Float
                || object instanceof Double
                || object instanceof Boolean
                || object instanceof String) {
            return String.valueOf(object);
        }
        return JsonUtil.getObjectToString(object);
    }

    /**
     * 修改key
     *
     * @param oldKey 旧的key
     * @param newKey 新的key
     */
    public void rename(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * 返回key存储的类型
     *
     * @param key
     */
    public String getType(String key) {
        return redisTemplate.type(key).code();
    }

    // ============================String=============================

    /**
     * 获取redis的String值
     *
     * @param key
     * @return
     */
    public Object getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // ============================Map=============================

    /**
     * 判断hash表中是否有对应的value
     *
     * @param hashId
     * @param key
     * @return
     */
    public boolean hasKey(String hashId, String key) {
        return redisTemplate.opsForHash().hasKey(hashId, key);
    }

    /**
     * 获取hashKey对应的所有键
     *
     * @param hashId 键
     */
    public List<String> getHashKeys(String hashId) {
        List<String> list = new ArrayList<>();
        Map<Object, Object> map = this.getMap(hashId);
        for (Object object : map.keySet()) {
            if (object instanceof String) {
                list.add(String.valueOf(object));
            }
        }
        return list;
    }

    /**
     * 获取hashKey对应的所有值
     *
     * @param hashId 键
     */
    public List<String> getHashValues(String hashId) {
        List<String> list = new ArrayList<>();
        Map<Object, Object> map = this.getMap(hashId);
        for (Object object : map.keySet()) {
            if (map.get(object) instanceof String) {
                list.add(String.valueOf(map.get(object)));
            }
        }
        return list;
    }

    /**
     * 查询具体map的值
     *
     * @param hashId
     * @param key
     * @return
     */
    public String getHashValues(String hashId, String key) {
        Object object = redisTemplate.opsForHash().get(hashId, key);
        if (object != null) {
            return String.valueOf(object);
        } else {
            return null;
        }
    }

    /**
     * 删除指定map的key
     *
     * @param key
     */
    public void removeHash(String hashId, String key) {
        if (hasKey(hashId, key)) {
            redisTemplate.opsForHash().delete(hashId, key);
        }
    }

    /**
     * 获取所有的map缓存
     *
     * @param key
     * @return
     */
    public <K, V> Map<K, V> getMap(String key) {
        return (Map<K, V>) redisTemplate.opsForHash().entries(key);
    }

    /**
     * 插入map的值
     *
     * @param hashId 主键id
     * @param key map的key
     * @param value map的值
     */
    public void insertHash(String hashId, String key, String value) {
        redisTemplate.opsForHash().put(hashId, key, value);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> getSet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long getSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key 键
     * @param start 开始 0 是第一个元素
     * @param end 结束 -1代表所有值
     * @return @取出来的元素 总数 end-start+1
     */
    public List<Object> get(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long getListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            log.error(key, e);
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key 键
     * @param index 索引 index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object getIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            log.error(key, e);
            return null;
        }
    }

    public Integer token(String tenantId) {
        Set<String> data = redisTemplate.keys(tenantId + CacheKeyUtil.LOGINONLINE + "*");
        return data.size();
    }
}
