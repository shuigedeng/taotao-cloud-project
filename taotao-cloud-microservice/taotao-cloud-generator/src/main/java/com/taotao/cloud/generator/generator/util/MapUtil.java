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

package com.taotao.cloud.generator.generator.util;

import java.util.Map;

/**
 * @author zhenkai.blog.csdn.net
 */
public class MapUtil {
    public static String getString(Map map, String key) {
        if (map != null && map.containsKey(key)) {
            try {
                return map.get(key).toString();
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        } else {
            return "";
        }
    }

    public static Integer getInteger(Map map, String key) {
        if (map != null && map.containsKey(key)) {
            try {
                return (Integer) map.get(key);
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static Boolean getBoolean(Map map, String key) {
        if (map != null && map.containsKey(key)) {
            try {
                return (Boolean) map.get(key);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }
}
