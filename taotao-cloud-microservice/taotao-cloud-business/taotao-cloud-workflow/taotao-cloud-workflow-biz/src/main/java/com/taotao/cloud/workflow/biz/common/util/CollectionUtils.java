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

package com.taotao.cloud.workflow.biz.common.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/** 集合处理工具类 */
public class CollectionUtils {

    /**
     * map key转小写
     *
     * @param requestMap
     * @return
     */
    public static Map<String, Object> mapKeyToLower(Map<String, ?> requestMap) {
        // 非空校验
        if (requestMap.isEmpty()) {
            return null;
        }
        // 初始化放转换后数据的Map
        Map<String, Object> responseMap = new HashMap<>(16);
        // 使用迭代器进行循环遍历
        Set<String> requestSet = requestMap.keySet();
        Iterator<String> iterator = requestSet.iterator();
        iterator.forEachRemaining(obj -> {
            // 判断Key对应的Value是否为Map
            if ((requestMap.get(obj) instanceof Map)) {
                // 递归调用，将value中的Map的key转小写
                responseMap.put(obj.toLowerCase(), mapKeyToLower((Map) requestMap.get(obj)));
            } else {
                // 直接将key小写放入responseMap
                responseMap.put(obj.toLowerCase(), requestMap.get(obj));
            }
        });

        return responseMap;
    }

    /**
     * 获取map中第一个数据值
     *
     * @param map 数据源
     * @return
     */
    public static Object getFirstOrNull(Map<String, Object> map) {
        Object obj = null;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }
}
