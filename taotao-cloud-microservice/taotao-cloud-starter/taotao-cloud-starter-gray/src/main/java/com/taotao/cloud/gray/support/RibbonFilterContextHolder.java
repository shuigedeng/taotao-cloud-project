package com.taotao.cloud.gray.support;


import com.taotao.cloud.gray.api.RibbonFilterContext;

/**
 * 灰度负载过滤上下文持有类
 * @author pangu
 * @since 2020-7-26
 */
public class RibbonFilterContextHolder {

    private static final ThreadLocal<RibbonFilterContext> CONTEXT_HOLDER = new InheritableThreadLocal() {
        @Override
        protected RibbonFilterContext initialValue() {
            return new DefaultRibbonFilterContext();
        }
    };

    public static RibbonFilterContext getCurrentContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void clearCurrentContext() {
        CONTEXT_HOLDER.remove();
    }
}
