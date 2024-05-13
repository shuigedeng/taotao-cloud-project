package com.taotao.cloud.cache.support.listener.slow;

import com.taotao.cloud.cache.api.ICacheSlowListener;
import com.taotao.cloud.cache.api.ICacheSlowListenerContext;
import com.taotao.cloud.cache.support.interceptor.common.CacheInterceptorCost;

/**
 * 慢日志监听类
 * @author shuigedeng
 * @since 0.0.9
 */
public class CacheSlowListener implements ICacheSlowListener {

    private static final Log log = LogFactory.getLog(CacheInterceptorCost.class);

    @Override
    public void listen(ICacheSlowListenerContext context) {
        log.warn("[Slow] methodName: {}, params: {}, cost time: {}",
                context.methodName(), JSON.toJSON(context.params()), context.costTimeMills());
    }

    @Override
    public long slowerThanMills() {
        return 1000L;
    }

}
