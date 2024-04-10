package com.taotao.cloud.gateway.skywalking;

import org.dromara.hutool.core.bean.BeanUtil;
import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import reactor.core.CoreSubscriber;
import reactor.util.context.Context;

import java.util.Optional;

public class MdcSubscriber implements CoreSubscriber {

    private static final String TRACE_ID = "traceId";

    private static final String SKYWALKING_CTX_SNAPSHOT = "SKYWALKING_CONTEXT_SNAPSHOT";

    private final CoreSubscriber<Object> actual;

    public MdcSubscriber(CoreSubscriber<Object> actual) {
        this.actual = actual;
    }

    @Override
    public void onSubscribe(Subscription s) {
        actual.onSubscribe(s);
    }

    @Override
    public void onNext(Object o) {
        Context c = actual.currentContext();
        Optional<String> traceIdOptional = Optional.empty();
        if (!c.isEmpty() && c.hasKey(SKYWALKING_CTX_SNAPSHOT)) {
            traceIdOptional = Optional.of(c.get(SKYWALKING_CTX_SNAPSHOT)).map(BeanUtil::beanToMap)
                    .map(t -> t.get(TRACE_ID)).map(BeanUtil::beanToMap).map(t -> t.get("id")).map(Object::toString);
        }
        try (MDC.MDCCloseable cMdc = MDC.putCloseable(TRACE_ID, traceIdOptional.orElse("N/A"))) {
            actual.onNext(o);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        actual.onError(throwable);
    }

    @Override
    public void onComplete() {
        actual.onComplete();
    }

    @Override
    public Context currentContext() {
        return actual.currentContext();
    }
}
