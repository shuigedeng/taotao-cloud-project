package com.taotao.cloud.agent.bytebuddyother.core.context;

import com.taotao.cloud.agent.demo.common.Logger;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator;
import io.opentelemetry.context.propagation.ContextPropagators;
import io.opentelemetry.context.propagation.TextMapPropagator;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.SdkTracerProviderBuilder;
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor;
import io.opentelemetry.sdk.trace.export.SpanExporter;

import java.util.List;

/**
 * Created by pphh on 2022/8/4.
 */
public class TraceContext {

    private static Tracer TracerInst;
    private static OpenTelemetry TelemetryInst;

    public static void initTraceContext(List<SpanExporter> spanExporters) {
        Logger.info("The core context is initializing...");

        SdkTracerProviderBuilder builder = SdkTracerProvider.builder();

        for (SpanExporter exporter : spanExporters) {
            builder.addSpanProcessor(BatchSpanProcessor.builder(exporter).build());
        }
        SdkTracerProvider tracerProvider = builder.build();

        TelemetryInst = OpenTelemetrySdk.builder()
                .setTracerProvider(tracerProvider)
                .setPropagators(ContextPropagators.create(W3CTraceContextPropagator.getInstance()))
                .buildAndRegisterGlobal();

        Logger.info("The core context has been initialized");
    }

    public static Tracer TRACER() {
        if (TracerInst == null) {
            synchronized (TraceContext.class) {
                TracerInst = TelemetryInst.getTracer("demo", "1.0.0");
            }
        }
        return TracerInst;
    }

    public static TextMapPropagator TextPropagator() {
        return TelemetryInst.getPropagators().getTextMapPropagator();
    }

}
