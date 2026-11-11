package com.taotao.cloud.rpc.spring.starter.config;

import com.taotao.cloud.rpc.spring.annotation.TtcRpcService;
import com.taotao.cloud.rpc.remoting.RpcServer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ttc-RPC 服务导出器
 * 扫描所有标了 {@link TtcRpcService} 的 Bean，调用 RpcServer.register 注册为 RPC 服务。
 */
@Component
public class TtcRpcServiceExporter implements BeanPostProcessor {

    private static final Logger log = LogManager.getLogger(TtcRpcServiceExporter.class);

    @Autowired(required = false)
    private RpcServer rpcServer;

    private final List<Runnable> pendingRegistrations = new ArrayList<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 必须先看"真实类"：CGLIB 代理子类的 getAnnotation 读不到父类上的注解，
        // 而它的 getInterfaces() 返回的是 SpringProxy/Advised 这类标记接口，
        // 拿它当服务名会把业务接口导出成一个没人认识的键。
        Class<?> userClass = ClassUtils.getUserClass(bean);
        TtcRpcService annotation = AnnotatedElementUtils.findMergedAnnotation(userClass, TtcRpcService.class);
        if (annotation == null) return bean;

        List<Class<?>> interfaces = new ArrayList<Class<?>>();
        if (annotation.interfaceClass() != void.class) {
            interfaces.add(annotation.interfaceClass());
        } else {
            // javadoc 承诺"不指定则使用该类实现的所有接口"，所以这里是全量而非 [0]
            for (Class<?> iface : ClassUtils.getAllInterfacesForClass(userClass)) {
                if (iface.getName().startsWith("java.") || iface.getName().startsWith("org.springframework.")) {
                    continue;
                }
                interfaces.add(iface);
            }
        }
        if (interfaces.isEmpty()) {
            log.warn("[TtcRpcService] {} has no interfaces, skipping", beanName);
            return bean;
        }

        for (Class<?> iface : interfaces) {
            export(bean, beanName, iface, annotation.version());
        }
        return bean;
    }

    private void export(final Object bean, final String beanName,
                        final Class<?> interfaceClass, final String version) {
        Runnable task = () -> {
            try {
                rpcServer.register(interfaceClass, bean, version);
                log.info("[TtcRpcService] exported {} -> {}", beanName, interfaceClass.getName());
            } catch (Exception e) {
                log.error("[TtcRpcService] failed to export " + beanName, e);
            }
        };

        if (rpcServer != null) {
            task.run();
        } else {
            // 暂存，等 RPC Server 启动后再注册
            synchronized (pendingRegistrations) {
                pendingRegistrations.add(task);
            }
        }
    }

    @EventListener(ContextRefreshedEvent.class)
    public void onContextRefreshed() {
        List<Runnable> tasks;
        synchronized (pendingRegistrations) {
            if (pendingRegistrations.isEmpty()) {
                return;
            }
            tasks = new ArrayList<Runnable>(pendingRegistrations);
            pendingRegistrations.clear();
        }
        if (rpcServer == null) {
            // 上下文刷新完还没有 RpcServer，就再也不会有了：这些 Runnable 各自强引用一个业务 bean，
            // 留在队列里等于把整个 bean 拽住不放，而它们本来一条也执行不了。
            log.error("[TtcRpcService] no RpcServer in this context; dropped {} pending registration(s), "
                    + "none of them are exported over RPC", tasks.size());
            return;
        }
        for (Runnable task : tasks) {
            task.run();
        }
    }
}
