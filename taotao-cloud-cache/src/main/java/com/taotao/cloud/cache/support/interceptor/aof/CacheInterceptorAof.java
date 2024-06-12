package com.taotao.cloud.cache.support.interceptor.aof;

import com.alibaba.fastjson2.JSON;
import com.taotao.cloud.cache.api.ICache;
import com.taotao.cloud.cache.api.ICacheInterceptor;
import com.taotao.cloud.cache.api.ICacheInterceptorContext;
import com.taotao.cloud.cache.api.ICachePersist;
import com.taotao.cloud.cache.model.PersistAofEntry;
import com.taotao.cloud.cache.support.persist.CachePersistAof;

/**
 * 顺序追加模式
 *
 * AOF 持久化到文件，暂时不考虑 buffer 等特性。
 * @author shuigedeng
 * @since 2024.06
 */
public class CacheInterceptorAof<K,V> implements ICacheInterceptor<K, V> {

    private static final Logger LOG = LoggerFactory.getLogger(CacheInterceptorAof.class);

    @Override
    public void before(ICacheInterceptorContext<K,V> context) {
    }

    @Override
    public void after(ICacheInterceptorContext<K,V> context) {
        // 持久化类
        ICache<K,V> cache = context.cache();
        ICachePersist<K,V> persist = cache.persist();

        if(persist instanceof CachePersistAof) {
            CachePersistAof<K,V> cachePersistAof = (CachePersistAof<K,V>) persist;

            String methodName = context.method().getName();
            PersistAofEntry aofEntry = PersistAofEntry.newInstance();
            aofEntry.setMethodName(methodName);
            aofEntry.setParams(context.params());

            String json = JSON.toJSONString(aofEntry);

            // 直接持久化
            log.debug("AOF 开始追加文件内容：{}", json);
            cachePersistAof.append(json);
            log.debug("AOF 完成追加文件内容：{}", json);
        }
    }

}
