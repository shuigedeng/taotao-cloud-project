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

package com.taotao.cloud.shell.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

// 自定义类型转换器
@Component
public class MyConverter implements Converter<String, Food> {
    @Override
    public Food convert(String s) {
        // 将输入参数转换为Food类型实例
        return new Food(s);
    }
}
