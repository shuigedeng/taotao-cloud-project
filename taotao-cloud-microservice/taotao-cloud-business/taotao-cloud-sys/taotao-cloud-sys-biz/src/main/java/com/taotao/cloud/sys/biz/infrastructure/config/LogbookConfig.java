package com.taotao.cloud.sys.biz.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.zalando.logbook.*;
import org.zalando.logbook.core.Conditions;
import org.zalando.logbook.core.DefaultHttpLogFormatter;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;

import java.io.IOException;

/**
 * LogbookConfig
 *
 * @author shuigedeng
 * @version 2026.04
 * @since 2025-12-19 09:30:45
 */
public class LogbookConfig {

    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .condition(Conditions.exclude(Conditions.requestTo("/users/*"),
                        Conditions.contentType("application/json")))
                .sink(new DefaultSink(new DefaultHttpLogFormatter(), new DefaultHttpLogWriter()))
                .sink(new Sink() {
                    @Override
                    public boolean isActive() {
                        return Sink.super.isActive();
                    }

                    @Override
                    public void write( Precorrelation precorrelation, HttpRequest request )
                            throws IOException {

                    }

                    @Override
                    public void write( Correlation correlation, HttpRequest request,
                            HttpResponse response )
                            throws IOException {

                    }

                    @Override
                    public void writeBoth( Correlation correlation, HttpRequest request,
                            HttpResponse response )
                            throws IOException {
                        System.err.println("==============================");
                        System.err.println("request header:\t" + request.getHeaders());
                        System.err.println("request body:\t" + request.getBodyAsString());
                        System.out.println();
                        System.err.println("response header:\t" + response.getHeaders());
                        System.err.println("response body:\t" + response.getBodyAsString());
                        System.err.println("==============================");
                    }
                })
                .build();
    }

}
