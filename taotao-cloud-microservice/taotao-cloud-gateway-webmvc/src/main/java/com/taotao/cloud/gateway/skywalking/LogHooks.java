// package com.taotao.cloud.gateway.skywalking;
//
// import jakarta.annotation.PostConstruct;
// import org.springframework.stereotype.Component;
// import reactor.core.publisher.Operators;
//
// import jakarta.annotation.PreDestroy;
//
// @Component
// public class LogHooks {
//
//    private static final String KEY = "logMdc";
//
//    @PostConstruct
//    @SuppressWarnings("unchecked")
//    public void setHook() {
//        reactor.core.publisher.Hooks.onEachOperator(KEY,
//                Operators.lift((scannable, coreSubscriber) -> new MdcSubscriber(coreSubscriber)));
//    }
//
//    @PreDestroy
//    public void resetHook() {
//        reactor.core.publisher.Hooks.resetOnEachOperator(KEY);
//    }
//
// }
