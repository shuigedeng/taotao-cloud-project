package com.taotao.cloud.cache.support.listener.remove;

import com.taotao.cloud.cache.api.ICacheRemoveListener;
import com.taotao.cloud.cache.api.ICacheRemoveListenerContext;

/**
 * 默认的删除监听类
 * @author shuigedeng
 * @since 0.0.6
 */
public class CacheRemoveListener<K,V> implements ICacheRemoveListener<K,V> {

    private static final Log log = LogFactory.getLog(CacheRemoveListener.class);

    @Override
    public void listen(ICacheRemoveListenerContext<K, V> context) {
        log.debug("Remove key: {}, value: {}, type: {}",
                context.key(), context.value(), context.type());
    }

}
