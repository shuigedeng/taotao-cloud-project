/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.p6spy.component;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * P6spy SQL 日志格式化
 *
 * @author dengtao
 * @since 2020/10/14 09:37
 * @version 1.0.0
 */
public class P6spyLogFormat implements MessageFormattingStrategy {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public String formatMessage(final int connectionId, final String now, final long elapsed, final String category, final String prepared, final String sql, final String url) {
        return !"".equals(sql.trim()) ? LocalDateTime.now().format(formatter) + " | took " + elapsed + "ms | " + category + " | connection " + connectionId + "\n " + sql + ";" : "";
    }
}
