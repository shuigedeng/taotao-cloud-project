package com.taotao.cloud.rpc.spring.starter.config;

import com.taotao.cloud.rpc.cluster.config.ReferenceConfig;
import com.taotao.cloud.rpc.spring.annotation.TtcRpcReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * ttc-RPC 引用注入器
 * 扫描所有 Bean 的字段和方法，发现 {@link TtcRpcReference} 后注入代理
 */
@Component
public class TtcRpcReferenceInjector implements InstantiationAwareBeanPostProcessor, ApplicationContextAware, BeanFactoryAware {

    private static final Logger log = LogManager.getLogger(TtcRpcReferenceInjector.class);

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(org.springframework.beans.factory.BeanFactory beanFactory) throws BeansException {
        // no-op
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
        // Spring 6（SB3）起 postProcessPropertyValues(PropertyValues, PropertyDescriptor[], ...) 已删除，
        // 统一走 postProcessProperties(PropertyValues, Object, String)。
        Class<?> targetClass = bean.getClass();
        ReflectionUtils.doWithFields(targetClass, field -> injectField(field, bean));
        return pvs;
    }

    private void injectField(Field field, Object bean) {
        TtcRpcReference annotation = field.getAnnotation(TtcRpcReference.class);
        if (annotation == null) return;
        // setAccessible(true) 在 JDK 8 与 9+ 上都成立；Field#canAccess(Object) 是 JDK 9 API，
        // 而本模块只声明 source/target=8（没有 --release），留着它会在真 JDK 8 上 NoSuchMethodError。
        field.setAccessible(true);
        try {
            ReferenceConfig<?> config = buildReferenceConfig(annotation, field.getType());
            Object proxy = config.get();
            field.set(bean, proxy);
            // 同时把代理注册成 Spring Bean，避免后续 @Autowired 失败
            registerAsBean(field.getType(), proxy);
            log.info("[TtcRpcReference] injected {} -> {}", field.getName(), field.getType().getName());
        } catch (Exception e) {
            log.error("Failed to inject TtcRpcReference: " + field.getName(), e);
        }
    }

    private final java.util.Set<String> registeredBeanNames = java.util.concurrent.ConcurrentHashMap.newKeySet();

    private void registerAsBean(Class<?> type, Object proxy) {
        String beanName = type.getName();
        if (!registeredBeanNames.add(beanName)) {
            return;
        }
        if (applicationContext != null && applicationContext instanceof org.springframework.context.support.GenericApplicationContext) {
            ((org.springframework.context.support.GenericApplicationContext) applicationContext)
                    .getBeanFactory().registerSingleton(beanName, proxy);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private ReferenceConfig<?> buildReferenceConfig(TtcRpcReference annotation, Class interfaceClass) {
        ReferenceConfig config = new ReferenceConfig();
        config.setInterfaceClass(interfaceClass);
        config.setInterfaceName(annotation.interfaceName().isEmpty() ? interfaceClass.getName() : annotation.interfaceName());
        config.setVersion(annotation.version());
        config.setGroup(annotation.group());
        config.setTimeout(annotation.timeout());
        config.setRetries(annotation.retries());
        config.setLoadbalance(annotation.loadbalance());
        config.setCluster(annotation.cluster());
        config.setRegistry(annotation.registry());
        // ReferenceConfig 明明有 setAsync/setOneway（420-432 行），此前却被无声丢弃，
        // 导致 README 里的 @TtcRpcReference(async = true) 没有任何异步效果。
        config.setAsync(annotation.async());
        config.setOneway(annotation.oneway());
        if (annotation.url() != null && !annotation.url().isEmpty()) {
            config.setUrl(annotation.url());
        }
        return config;
    }
}
