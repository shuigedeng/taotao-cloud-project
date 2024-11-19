package com.taotao.cloud.message.biz.austin.handler.loadbalance.annotations;

import com.taotao.cloud.message.biz.austin.handler.enums.LoadBalancerStrategy;
import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * 负载均衡策略
 * @Author Gavin
 * @Date 2024/9/14
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface LoadBalancer {

    String loadbalancer() default LoadBalancerStrategy.SERVICE_LOAD_BALANCER_RANDOM_WEIGHT_ENHANCED;
}
