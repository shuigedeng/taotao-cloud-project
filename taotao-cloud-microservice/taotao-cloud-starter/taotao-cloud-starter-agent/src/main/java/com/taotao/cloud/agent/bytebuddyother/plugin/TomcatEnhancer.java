package com.taotao.cloud.agent.bytebuddyother.plugin;

import com.taotao.cloud.agent.bytebuddyother.agent.enhancer.AbstractEnhancer;
import com.taotao.cloud.agent.bytebuddyother.core.aspect.IAspectEnhancer;
import com.taotao.cloud.agent.bytebuddyother.core.context.TraceContext;
import com.taotao.cloud.agent.demo.common.Logger;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanKind;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapGetter;
import org.apache.catalina.connector.Request;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by pphh on 2022/8/4.
 */
public class TomcatEnhancer extends AbstractEnhancer {

    private static ThreadLocal<Span> spanThreadLocal = new ThreadLocal<>();

    @Override
    public void beforeMethod(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object result) throws Throwable {
        Logger.info("[trace]beforeMethod(), method = %s.%s", method.getDeclaringClass().getName(), method.getName());
        Request request = (Request) allArguments[0];

        TextMapGetter<Request> getter =
                new TextMapGetter<Request>() {
                    @Override
                    public String get(Request carrier, String key) {
                        return carrier.getHeader(key);
                    }

                    @Override
                    public Iterable<String> keys(Request carrier) {
                        List<String> keys = new ArrayList<>();
                        for (final Enumeration<String> headers = carrier.getParameterNames(); headers.hasMoreElements(); ) {
                            final String name = headers.nextElement();
                            keys.add(name);
                        }
                        return keys;
                    }
                };

        Context extractedContext = TraceContext.TextPropagator().extract(Context.current(), request, getter);
        extractedContext.makeCurrent();

        String operationName = String.format("%s %s", request.getMethod(), request.getRequestURI());
        Span span = TraceContext.TRACER().spanBuilder(operationName)
                .setSpanKind(SpanKind.PRODUCER)
                .startSpan()
                .setAttribute("URL", request.getRequestURL().toString())
                .setAttribute("METHOD", request.getMethod())
                .setAttribute("URI", request.getRequestURI())
                .setAttribute("RemoteAddr", request.getRemoteAddr())
                .setAttribute("RemoteHost", request.getRemoteHost())
                .setAttribute("RemotePort", request.getRemotePort())
                .setAttribute("RemoteUser", request.getRemoteUser())
                .setAttribute("Component", "Tomcat");
        span.makeCurrent();
        spanThreadLocal.set(span);
    }

    @Override
    public Object afterMethod(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Object result) throws Throwable {
        Logger.info("[trace]afterMethod(), method = %s.%s", method.getDeclaringClass().getName(), method.getName());
        endCurrenSpan();
        return null;
    }

    @Override
    public void handleMethodException(Object objInst, Method method, Object[] allArguments, Class<?>[] argumentsTypes, Throwable t) {
        Logger.info("[trace]handleMethodException(), method = %s.%s", method.getDeclaringClass().getName(), method.getName());
        endCurrenSpan();
    }

    private void endCurrenSpan() {
        Span span = spanThreadLocal.get();
        if (span != null) {
            span.end();
            spanThreadLocal.remove();
        }
    }

}
