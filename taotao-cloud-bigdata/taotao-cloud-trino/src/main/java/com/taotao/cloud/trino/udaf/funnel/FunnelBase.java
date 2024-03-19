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

package com.taotao.cloud.trino.udaf.funnel;

import io.airlift.slice.Slice;
import io.airlift.slice.Slices;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 漏斗基础信息
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2021/1/25 下午3:44
 */
public class FunnelBase {

    // 查询的漏斗事件和索引关系 {events: {event: index, ....}, ....}
    public static Map<Slice, Map<Slice, Byte>> event_pos_dict = new HashMap<>();

    public static void initEvents(Slice events) {
        // 漏斗事件之间用 , 分割
        List<String> fs = Arrays.asList(new String(events.getBytes()).split(","));

        Map<Slice, Byte> postDict = new HashMap<>();
        for (byte i = 0; i < fs.size(); i++) {
            postDict.put(Slices.utf8Slice(fs.get(i)), i);
        }
        event_pos_dict.put(events, postDict);
    }
}
