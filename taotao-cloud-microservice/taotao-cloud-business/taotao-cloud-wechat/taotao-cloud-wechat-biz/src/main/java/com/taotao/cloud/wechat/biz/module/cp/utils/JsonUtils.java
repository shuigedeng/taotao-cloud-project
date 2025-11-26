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

package com.taotao.cloud.wechat.biz.module.cp.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonMapper;
import tools.jackson.databind.SerializationFeature;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class JsonUtils {
    private static final JsonMapper JSON = new JsonMapper();

    static {
        JSON.setSerializationInclusion(Include.NON_NULL);
        JSON.configure(SerializationFeature.INDENT_OUTPUT, Boolean.TRUE);
    }

    public static String toJson(Object obj) {
        try {
            return JSON.writeValueAsString(obj);
        } catch (JacksonException e) {
            LogUtils.error(e);
        }

        return null;
    }
}
