package com.taotao.cloud.gateway.filter.global;

import com.taotao.boot.common.utils.log.LogUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * QpsStatisticsFilter
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Component
public class QpsStatisticsFilter implements GlobalFilter, Ordered {

    // 存储接口QPS：key=接口路径，value=原子计数器
    private final Map<String, AtomicLong> pathQpsMap = new ConcurrentHashMap<>();

    // 定时1秒清零计数器（避免数值过大）
    @PostConstruct
    public void init() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            // 遍历所有接口，打印QPS后清零
            pathQpsMap.forEach(( path, counter ) -> {
                long qps = counter.getAndSet(0);
                LogUtils.info("接口[{}] QPS: {}", path, qps);
            });
        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    public Mono<Void> filter( ServerWebExchange exchange, GatewayFilterChain chain ) {
        // 获取请求路径（如/order/seckill）
        String path = exchange.getRequest().getPath().value();
        // 计数器自增（线程安全）
        pathQpsMap.computeIfAbsent(path, k -> new AtomicLong()).incrementAndGet();
        // 继续转发请求
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1; // 过滤器优先级：数字越小越先执行
    }
}
