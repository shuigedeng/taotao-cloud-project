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

package com.taotao.cloud.ccsr.common.log;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类，规范打日志格式、日志level
 */
public class Log {

    /**
     * 普通日志
     */
    public static final Logger INFO = LoggerFactory.getLogger("INFO");

    /**
     * 警告日志
     */
    public static final Logger WARING = LoggerFactory.getLogger("WARING");

    /**
     * 异常日志、错误日志
     */
    public static final Logger ERROR = LoggerFactory.getLogger("ERROR");

    /**
     * 打印日志调试使用
     */
    public static final Logger DEBUG = LoggerFactory.getLogger("DEBUG");

    /**
     * 默认日志模板
     * {className}#{methodName}|{logMsg}
     */
    private static final String DEFAULT_TEMPLATE = "|{}#{}|";

    public static void print(String msg) {
        //        debug(msg);
        System.out.println(msg);
    }

    public static void print(String msgTemplate, Object... msgParams) {
        //        debug(msgTemplate, msgParams);
        System.out.printf((msgTemplate) + "%n", msgParams);
    }

    public static void debug(String msg) {
        if (DEBUG.isDebugEnabled()) {
            StackTraceElement traceElement = Thread.currentThread().getStackTrace()[2];
            debug(traceElement.getClassName(), traceElement.getMethodName(), msg);
        }
    }

    public static void debug(String msgTemplate, Object... msgParams) {
        if (DEBUG.isInfoEnabled()) {
            StackTraceElement traceElement = Thread.currentThread().getStackTrace()[2];
            info(traceElement.getClassName(), traceElement.getMethodName(), msgTemplate, msgParams);
        }
    }

    /**
     * 记录日志
     *
     * @param msg 日志内容
     */
    public static void info(String msg) {
        if (INFO.isInfoEnabled()) {
            StackTraceElement traceElement = Thread.currentThread().getStackTrace()[2];
            info(traceElement.getClassName(), traceElement.getMethodName(), msg);
        }
    }

    /**
     * 记录日志
     *
     * @param msgTemplate 日志格式模版
     * @param msgParams   日志参数
     */
    public static void info(String msgTemplate, Object... msgParams) {
        if (INFO.isInfoEnabled()) {
            StackTraceElement traceElement = Thread.currentThread().getStackTrace()[2];
            info(traceElement.getClassName(), traceElement.getMethodName(), msgTemplate, msgParams);
        }
    }

    /**
     * 记录日志
     *
     * @param msg 消息内容
     */
    public static void warn(String msg) {
        if (WARING.isWarnEnabled()) {
            StackTraceElement traceElement = Thread.currentThread().getStackTrace()[2];
            warn(traceElement.getClassName(), traceElement.getMethodName(), msg);
        }
    }

    /**
     * 记录日志内容
     *
     * @param msgTemplate 日志格式
     * @param msgParams   日志参数
     */
    public static void warn(String msgTemplate, Object... msgParams) {
        if (WARING.isWarnEnabled()) {
            StackTraceElement traceElement = Thread.currentThread().getStackTrace()[2];
            warn(traceElement.getClassName(), traceElement.getMethodName(), msgTemplate, msgParams);
        }
    }

    public static void error(String msg, Throwable e) {
        if (ERROR.isErrorEnabled()) {
            StackTraceElement traceElement = Thread.currentThread().getStackTrace()[2];
            errorInner(traceElement.getClassName(), traceElement.getMethodName(), msg, e);
        }
    }

    /**
     * 记录日志内容
     *
     * @param msgTemplate 日志格式
     * @param msgParams   日志参数，最后一个参数是异常即可；异常不需要给占位符
     */
    public static void error(String msgTemplate, Object... msgParams) {
        if (ERROR.isErrorEnabled()) {
            StackTraceElement traceElement = Thread.currentThread().getStackTrace()[2];
            errorInner(
                    traceElement.getClassName(),
                    traceElement.getMethodName(),
                    msgTemplate,
                    msgParams);
        }
    }

    /**
     * debug记录日志，内部方法
     *
     * @param className   类名
     * @param methodName  方法名
     * @param msgTemplate 日志格式
     * @param msgParams   参数
     */
    private static void debug(
            String className, String methodName, String msgTemplate, Object... msgParams) {
        List<Object> params = convertParams(className, methodName, msgTemplate, msgParams);
        if (params == null) {
            return;
        }

        DEBUG.debug(DEFAULT_TEMPLATE + msgTemplate, params.toArray());
    }

    /**
     * info记录日志，内部方法
     *
     * @param className   类名
     * @param methodName  方法名
     * @param msgTemplate 日志格式
     * @param msgParams   参数
     */
    private static void info(
            String className, String methodName, String msgTemplate, Object... msgParams) {
        List<Object> params = convertParams(className, methodName, msgTemplate, msgParams);
        if (params == null) {
            return;
        }

        INFO.info(DEFAULT_TEMPLATE + msgTemplate, params.toArray());
    }

    /**
     * warn记录日志，内部方法
     *
     * @param className   类名
     * @param methodName  方法名
     * @param msgTemplate 日志格式
     * @param msgParams   参数
     */
    private static void warn(
            String className, String methodName, String msgTemplate, Object... msgParams) {
        List<Object> params = convertParams(className, methodName, msgTemplate, msgParams);
        if (params == null) {
            return;
        }

        WARING.warn(DEFAULT_TEMPLATE + msgTemplate, params.toArray());
    }

    /**
     * error记录日志，内部方法
     *
     * @param className   类名
     * @param methodName  方法名
     * @param msgTemplate 日志格式
     * @param msgParams   参数，最后一个参数可以是异常对象
     */
    private static void errorInner(
            String className, String methodName, String msgTemplate, Object... msgParams) {
        List<Object> params = convertParams(className, methodName, msgTemplate, msgParams);
        if (params == null) {
            return;
        }

        ERROR.error(DEFAULT_TEMPLATE + msgTemplate, params.toArray());
    }

    private static List<Object> convertParams(
            String className, String methodName, String msgTemplate, Object... msgParams) {
        if (StringUtils.isAnyBlank(className, methodName, msgTemplate)) {
            return null;
        }

        List<Object> params = new LinkedList<>();
        params.add(className);
        params.add(methodName);

        if (msgParams != null) {
            params.addAll(Arrays.asList(msgParams));
        }

        return params;
    }
}
