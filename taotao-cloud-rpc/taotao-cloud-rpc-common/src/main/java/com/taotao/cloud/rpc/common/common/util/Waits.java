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

package com.taotao.cloud.rpc.common.common.util;

import java.util.concurrent.TimeUnit;

/**
 * @since 0.1.3
 */
// @CommonEager
public final class Waits {

    /**
     * 等待指定的时间
     * @param time 时间
     * @param timeUnit 单位
     * @since 0.1.3
     */
    public static void waits(final long time, final TimeUnit timeUnit) {
        try {
            timeUnit.sleep(time);
        } catch (InterruptedException e) {
            //            throw new CommonRuntimeException(e);
        }
    }

    /**
     * 等待指定的时间 s
     * @param time 时间
     * @since 0.1.3
     */
    public static void waits(final long time) {
        waits(time, TimeUnit.SECONDS);
    }
}
