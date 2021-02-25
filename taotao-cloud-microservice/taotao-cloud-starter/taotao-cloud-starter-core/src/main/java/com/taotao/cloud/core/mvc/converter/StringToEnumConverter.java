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
package com.taotao.cloud.core.mvc.converter;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.taotao.cloud.common.enums.BaseEnum;
import org.springframework.core.convert.converter.Converter;

import java.util.Map;

/**
 * <p>
 * String枚举转化器
 * </p>
 *
 * @author dengtao
 * @date 2020/9/29 14:20
 * @since v1.0
 */
public class StringToEnumConverter<T extends BaseEnum> implements Converter<String, T> {

    private final Map<String, T> enumMap = MapUtil.newHashMap();

    public StringToEnumConverter(Class<T> enumType) {
        T[] enums = enumType.getEnumConstants();
        for (T e : enums) {
            Integer code = e.getCode();
            enumMap.put(e.getNameByCode(code), e);
        }
    }

    @Override
    public T convert(String source) {
        T t = enumMap.get(source);
        if (ObjectUtil.isNull(t)) {
            throw new IllegalArgumentException("无法匹配对应的枚举类型");
        }
        return t;
    }
}
