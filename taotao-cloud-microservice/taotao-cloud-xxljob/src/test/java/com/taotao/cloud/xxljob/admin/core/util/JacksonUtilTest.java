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

package com.taotao.cloud.xxljob.admin.core.util;

import static com.xxl.job.admin.core.util.JacksonUtil.writeValueAsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class JacksonUtilTest {

    @Test
    public void shouldWriteValueAsString() {
        // given
        Map<String, String> map = new HashMap<>();
        map.put("aaa", "111");
        map.put("bbb", "222");

        // when
        String json = writeValueAsString(map);

        // then
        assertEquals(json, "{\"aaa\":\"111\",\"bbb\":\"222\"}");
    }

    @Test
    public void shouldReadValueAsObject() {
        // given
        String jsonString = "{\"aaa\":\"111\",\"bbb\":\"222\"}";

        // when
        // Map result = JacksonUtil.readValue(jsonString, Map.class);
        //
        //// then
        // assertEquals(result.get("aaa"), "111");
        // assertEquals(result.get("bbb"),"222");

    }
}
