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

package com.taotao.cloud.message.biz.austin.cron.xxl.enums;

/**
 * 执行阻塞队列
 *
 * @author 3y
 */
public enum ExecutorBlockStrategyEnum {
    /** 单机串行 */
    SERIAL_EXECUTION,

    /** 丢弃后续调度 */
    DISCARD_LATER,

    /** 覆盖之前调度 */
    COVER_EARLY;

    ExecutorBlockStrategyEnum() {}
}
