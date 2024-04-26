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

package com.taotao.cloud.auth.infrastructure.extension.oneClick.mobtech.utils;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

public class BaseUtils {

    public static boolean isEmpty(Object target) {
        if (target == null) {
            return true;
        }
        if (target instanceof String && target.equals("")) {
            return true;
        } else if (target instanceof Collection) {
            return ((Collection<?>) target).isEmpty();
        } else if (target instanceof Map) {
            return ((Map<?, ?>) target).isEmpty();
        } else if (target.getClass().isArray()) {
            return Array.getLength(target) == 0;
        }
        return false;
    }
}
