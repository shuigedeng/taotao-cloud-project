package com.taotao.cloud.rpc.common.spi;

import com.taotao.cloud.rpc.common.spi.ExtensionFactory;
import com.taotao.cloud.rpc.common.spi.AdaptiveExtensionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * SPI 工厂（从 Spring 容器获取扩展对象）
 * <p>
 * 当项目集成 Spring 后，{@code SpiExtensionFactory} 可以从 Spring 容器中按 name 查找 bean。
 * <p>
 * 在没有 Spring 容器时返回 null，由 {@link AdaptiveExtensionFactory} 的其他工厂兜底。
 */
public class SpiExtensionFactory implements ExtensionFactory {

    private static final Logger log = LogManager.getLogger(SpiExtensionFactory.class);

    /**
     * Spring 上下文缓存，按 className -> ApplicationContext
     */
    private static final ConcurrentMap<String, Object> CONTEXT_CACHE = new ConcurrentHashMap<>();

    /**
     * 设置 Spring 应用上下文
     */
    public static void setApplicationContext(Object applicationContext) {
        if (applicationContext == null) {
            return;
        }
        CONTEXT_CACHE.put(applicationContext.getClass().getName(), applicationContext);
    }

    @Override
    public <T> T getExtension(Class<?> type, String name) {
        // 检查 Spring 容器中是否有指定 bean
        for (Object context : CONTEXT_CACHE.values()) {
            try {
                // 尝试通过反射调用 getBean
                Object bean = invokeGetBean(context, name);
                if (bean != null && type.isInstance(bean)) {
                    @SuppressWarnings("unchecked")
                    T result = (T) bean;
                    return result;
                }
            } catch (Throwable t) {
                log.debug("Failed to get bean '{}' from context: {}", name, t.getMessage());
            }
        }
        return null;
    }

    /**
     * 通过反射调用 ApplicationContext.getBean(name)
     */
    private Object invokeGetBean(Object context, String name) {
        try {
            return context.getClass().getMethod("getBean", String.class).invoke(context, name);
        } catch (NoSuchMethodException e) {
            // 不是 Spring 容器
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
