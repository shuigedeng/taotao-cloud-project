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
package com.taotao.cloud.common.utils;


import com.taotao.cloud.common.exception.BaseException;
import lombok.experimental.UtilityClass;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * ExceptionUtils
 *
 * @author dengtao
 * @date 2020/6/2 16:35
 * @since v1.0
 **/
@UtilityClass
public class ExceptionUtil {
    /**
     * trace2String
     *
     * @param t t
     * @return java.lang.String
     * @author dengtao
     * @date 2020/10/15 14:59
     * @since v1.0
     */
    public static String trace2String(Throwable t) {
        if (t == null) {
            return "";
        }
        try {
            try (StringWriter sw = new StringWriter()) {
                try (PrintWriter pw = new PrintWriter(sw, true)) {
                    t.printStackTrace(pw);
                    return sw.getBuffer().toString();
                }
            }
        } catch (Exception exp) {
            throw new BaseException(exp.getMessage());
        }
    }

    /**
     * trace2String
     *
     * @param stackTraceElements stackTraceElements
     * @return java.lang.String
     * @author dengtao
     * @date 2020/10/15 15:00
     * @since v1.0
     */
    public static String trace2String(StackTraceElement[] stackTraceElements) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement stackTraceElemen : stackTraceElements) {
            sb.append(stackTraceElemen.toString()).append("\n");
        }
        return sb.toString();
    }
}
