package com.taotao.cloud.cache.support.load;

import com.alibaba.fastjson2.JSON;
import com.taotao.boot.common.utils.common.StringUtil;
import com.taotao.boot.common.utils.io.FileUtils;
import com.taotao.boot.common.utils.lang.ObjectUtils;
import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.api.ICacheLoad;
import com.taotao.cloud.cache.model.PersistRdbEntry;

import java.util.List;

import com.xkzhangsan.time.utils.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 加载策略-文件路径
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheLoadDbJson<K,V> implements ICacheLoad<K,V> {

    private static final Logger log = LoggerFactory.getLogger(CacheLoadDbJson.class);

    /**
     * 文件路径
     * @since 2024.06
     */
    private final String dbPath;

    public CacheLoadDbJson(String dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public void load(ICache<K, V> cache) {
        List<String> lines = FileUtils.readAllLines(dbPath);
        log.info("[load] 开始处理 path: {}", dbPath);
        if(CollectionUtil.isEmpty(lines)) {
            log.info("[load] path: {} 文件内容为空，直接返回", dbPath);
            return;
        }

        for(String line : lines) {
            if(StringUtil.isEmpty(line)) {
                continue;
            }

            // 执行
            // 简单的类型还行，复杂的这种反序列化会失败
            PersistRdbEntry<K,V> entry = JSON.parseObject(line, PersistRdbEntry.class);

            K key = entry.getKey();
            V value = entry.getValue();
            Long expire = entry.getExpire();

            cache.put(key, value);
            if(ObjectUtils.isNotNull(expire)) {
                cache.expireAt(key, expire);
            }
        }
        //nothing...
    }
}
