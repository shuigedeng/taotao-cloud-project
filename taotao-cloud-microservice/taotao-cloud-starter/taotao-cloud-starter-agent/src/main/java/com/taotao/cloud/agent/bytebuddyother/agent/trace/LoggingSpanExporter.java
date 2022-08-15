package com.taotao.cloud.agent.bytebuddyother.agent.trace;

import com.taotao.cloud.agent.bytebuddyother.core.log.Logger;
import io.opentelemetry.sdk.common.CompletableResultCode;
import io.opentelemetry.sdk.common.InstrumentationLibraryInfo;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SpanExporter;

import java.util.Collection;

/**
 * Created by pphh on 2022/8/4.
 */
public class LoggingSpanExporter implements SpanExporter {

    @Override
    public CompletableResultCode export(Collection<SpanData> spans) {
        for (SpanData span : spans) {
            InstrumentationLibraryInfo instruInfo = span.getInstrumentationLibraryInfo();
            Logger.info("%s %s - %s",
                    instruInfo.getName(),
                    instruInfo.getVersion(),
                    span.toString());
        }
        return CompletableResultCode.ofSuccess();
    }

    @Override
    public CompletableResultCode flush() {
        return null;
    }

    @Override
    public CompletableResultCode shutdown() {
        return null;
    }
}
