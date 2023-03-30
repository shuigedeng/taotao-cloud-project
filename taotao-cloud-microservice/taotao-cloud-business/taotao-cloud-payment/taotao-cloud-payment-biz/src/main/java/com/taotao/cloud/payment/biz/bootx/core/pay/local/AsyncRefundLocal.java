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

package com.taotao.cloud.payment.biz.bootx.core.pay.local;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * 异步退款线程变量
 *
 * @author xxm
 * @date 2022/3/9
 */
public final class AsyncRefundLocal {
    private static final ThreadLocal<String> THREAD_LOCAL = new TransmittableThreadLocal<>();
    private static final ThreadLocal<String> ERROR_MSG = new TransmittableThreadLocal<>();
    private static final ThreadLocal<String> ERROR_CODE = new TransmittableThreadLocal<>();
    /** 设置 */
    public static void set(String refundId) {
        THREAD_LOCAL.set(refundId);
    }

    /** 获取 */
    public static String get() {
        return THREAD_LOCAL.get();
    }

    /** 设置 */
    public static void setErrorMsg(String errorMsg) {
        ERROR_MSG.set(errorMsg);
    }

    /** 获取 */
    public static String getErrorMsg() {
        return ERROR_MSG.get();
    }
    /** 设置 */
    public static void setErrorCode(String errorCode) {
        ERROR_CODE.set(errorCode);
    }

    /** 获取 */
    public static String getErrorCode() {
        return ERROR_CODE.get();
    }

    /** 清除 */
    public static void clear() {
        THREAD_LOCAL.remove();
        ERROR_MSG.remove();
        ERROR_CODE.remove();
    }
}
