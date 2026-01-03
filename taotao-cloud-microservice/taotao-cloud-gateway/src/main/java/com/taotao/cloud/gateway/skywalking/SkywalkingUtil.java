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

package com.taotao.cloud.gateway.skywalking;

import cn.hutool.core.util.ObjectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.server.ServerWebExchange;

/**
 * SkywalkingUtil
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class SkywalkingUtil {

    /**
     * tid放入MDC
     */
    public static void putTidIntoMdc( ServerWebExchange exchange ) {
        putTidIntoMdc(exchange, "tid");
    }

    /**
     * tid放入MDC
     */
    public static void putTidIntoMdc( ServerWebExchange exchange, String key ) {
        try {
            Object entrySpanInstance = exchange.getAttributes().get("SKYWALKING_SPAN");
            if (ObjectUtil.isEmpty(entrySpanInstance)) {
                return;
            }
            Class<?> entrySpanClazz = entrySpanInstance.getClass().getSuperclass().getSuperclass();
            Field field = entrySpanClazz.getDeclaredField("owner");
            field.setAccessible(true);
            Object ownerInstance = field.get(entrySpanInstance);
            Class<?> ownerClazz = ownerInstance.getClass();
            Method getTraceId = ownerClazz.getMethod("getReadablePrimaryTraceId");
            String traceId = (String) getTraceId.invoke(ownerInstance);
            MDC.put(key, traceId);
        } catch (Exception e) {
            log.error("gateway追踪码获取失败", e);
        }
    }
}
