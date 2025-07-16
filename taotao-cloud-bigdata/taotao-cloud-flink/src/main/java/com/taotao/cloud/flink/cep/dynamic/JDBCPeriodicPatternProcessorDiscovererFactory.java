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

package com.taotao.cloud.flink.cep.dynamic;

import static java.util.Objects.requireNonNull;

import java.util.List;
import javax.annotation.Nullable;
import org.apache.flink.cep.dynamic.processor.PatternProcessor;
import org.apache.flink.cep.dynamic.processor.PatternProcessorDiscoverer;

/**
 * The JDBC implementation of the {@link PeriodicPatternProcessorDiscovererFactory} that creates the
 * {@link JDBCPeriodicPatternProcessorDiscoverer} instance.
 *
 * @param <T> Base type of the elements appearing in the pattern.
 */
public class JDBCPeriodicPatternProcessorDiscovererFactory<T>
        extends PeriodicPatternProcessorDiscovererFactory<T> {

    private final String jdbcUrl;
    private final String jdbcDriver;
    private final String tableName;

    public JDBCPeriodicPatternProcessorDiscovererFactory(
            final String jdbcUrl,
            final String jdbcDriver,
            final String tableName,
            @Nullable final List<PatternProcessor<T>> initialPatternProcessors,
            @Nullable final Long intervalMillis) {
        super(initialPatternProcessors, intervalMillis);
        this.jdbcUrl = requireNonNull(jdbcUrl);
        this.jdbcDriver = requireNonNull(jdbcDriver);
        this.tableName = requireNonNull(tableName);
    }

    @Override
    public PatternProcessorDiscoverer<T> createPatternProcessorDiscoverer(
            ClassLoader userCodeClassLoader) throws Exception {
        return new JDBCPeriodicPatternProcessorDiscoverer<>(
                jdbcUrl,
                jdbcDriver,
                tableName,
                userCodeClassLoader,
                this.getInitialPatternProcessors(),
                getIntervalMillis());
    }
}
