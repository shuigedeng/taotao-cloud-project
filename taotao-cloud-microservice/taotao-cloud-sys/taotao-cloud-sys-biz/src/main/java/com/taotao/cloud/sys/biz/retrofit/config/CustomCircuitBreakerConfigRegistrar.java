//package com.taotao.cloud.sys.biz.retrofit.model;
//
//import com.github.lianjiatech.retrofit.spring.boot.core.Constants;
//import com.github.lianjiatech.retrofit.spring.boot.degrade.resilience4j.CircuitBreakerConfigRegistrar;
//import com.github.lianjiatech.retrofit.spring.boot.degrade.resilience4j.CircuitBreakerConfigRegistry;
//import org.springframework.stereotype.Component;
//
//@Component
//public class CustomCircuitBreakerConfigRegistrar implements CircuitBreakerConfigRegistrar {
//   @Override
//   public void register(CircuitBreakerConfigRegistry registry) {
//
//         // 替换默认的CircuitBreakerConfig
//         registry.register(Constants.DEFAULT_CIRCUIT_BREAKER_CONFIG, CircuitBreakerConfig.ofDefaults());
//
//         // 注册其它的CircuitBreakerConfig
//         registry.register("testCircuitBreakerConfig", CircuitBreakerConfig.custom()
//                 .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.TIME_BASED)
//                 .failureRateThreshold(20)
//                 .minimumNumberOfCalls(5)
//                 .permittedNumberOfCallsInHalfOpenState(5)
//                 .build());
//   }
//}
