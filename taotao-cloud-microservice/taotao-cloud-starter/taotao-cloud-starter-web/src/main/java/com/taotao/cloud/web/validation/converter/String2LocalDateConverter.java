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
package com.taotao.cloud.web.validation.converter;

import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_FORMAT_EN;
import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_FORMAT_EN_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtil.DEFAULT_DATE_FORMAT_MATCHES;
import static com.taotao.cloud.common.utils.date.DateUtil.SLASH_DATE_FORMAT;
import static com.taotao.cloud.common.utils.date.DateUtil.SLASH_DATE_FORMAT_MATCHES;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * String2LocalDateConverter
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 22:11:42
 */
public class String2LocalDateConverter extends BaseDateConverter<LocalDate> implements Converter<String, LocalDate> {

    /**
     * FORMAT
     */
    protected static final Map<String, String> FORMAT = new LinkedHashMap<>(5);

    static {
        FORMAT.put(DEFAULT_DATE_FORMAT, DEFAULT_DATE_FORMAT_MATCHES);
        FORMAT.put(SLASH_DATE_FORMAT, SLASH_DATE_FORMAT_MATCHES);
        FORMAT.put(DEFAULT_DATE_FORMAT_EN, DEFAULT_DATE_FORMAT_EN_MATCHES);
    }

    @Override
    protected Map<String, String> getFormat() {
        return FORMAT;
    }

    @Override
    public LocalDate convert(String source) {
        return super.convert(source, (key) -> LocalDate.parse(source, DateTimeFormatter.ofPattern(key)));
    }

}

