/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.gateway.skywalking;

import java.util.Optional;

import cn.hutool.core.bean.BeanUtil;
import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import reactor.core.CoreSubscriber;
import reactor.util.context.Context;

/**
 * MdcSubscriber
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class MdcSubscriber implements CoreSubscriber {

    private static final String TRACE_ID = "traceId";

    private static final String SKYWALKING_CTX_SNAPSHOT = "SKYWALKING_CONTEXT_SNAPSHOT";

    private final CoreSubscriber<Object> actual;

    public MdcSubscriber( CoreSubscriber<Object> actual ) {
        this.actual = actual;
    }

    @Override
    public void onSubscribe( Subscription s ) {
        actual.onSubscribe(s);
    }

    @Override
    public void onNext( Object o ) {
        Context c = actual.currentContext();
        Optional<String> traceIdOptional = Optional.empty();
        if (!c.isEmpty() && c.hasKey(SKYWALKING_CTX_SNAPSHOT)) {
            traceIdOptional =
                    Optional.of(c.get(SKYWALKING_CTX_SNAPSHOT))
                            .map(BeanUtil::beanToMap)
                            .map(t -> t.get(TRACE_ID))
                            .map(BeanUtil::beanToMap)
                            .map(t -> t.get("id"))
                            .map(Object::toString);
        }
        try (MDC.MDCCloseable cMdc = MDC.putCloseable(TRACE_ID, traceIdOptional.orElse("N/A"))) {
            actual.onNext(o);
        }
    }

    @Override
    public void onError( Throwable throwable ) {
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
