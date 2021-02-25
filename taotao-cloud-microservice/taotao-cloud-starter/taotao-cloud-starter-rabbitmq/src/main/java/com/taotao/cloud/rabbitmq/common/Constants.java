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
package com.taotao.cloud.rabbitmq.common;

/**
 * 静态常量
 *
 * @author dengtao
 * @since 2020/5/28 17:19
 * @version 1.0.0
 */
public class Constants {
    /**
     * 线程数
     */
    public final static int THREAD_COUNT = 5;

    /**
     * 处理间隔时间
     */
    public final static int INTERVAL_MILS = 0;

    /**
     * consumer失败后等待时间(mils)
     */
    public static final int ONE_SECOND = 1 * 1000;

    /**
     * 异常sleep时间(mils)
     */
    public static final int ONE_MINUTE = 1 * 60 * 1000;
    /**
     * MQ消息retry时间
     */
    public static final int RETRY_TIME_INTERVAL = ONE_MINUTE;
    /**
     * MQ消息有效时间
     */
    public static final int VALID_TIME = ONE_MINUTE;

    /**
     * 主题模式
     */
    public static final String TOPIC_TYPE = "topic";

    /**
     *
     */
    public static final String DIRECT_TYPE = "direct";
}
