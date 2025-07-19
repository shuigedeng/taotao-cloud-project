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

package com.taotao.cloud.realtime.datalake.mall.common;

/**
 *
 * Date: 2021/2/2
 * Desc: 项目配置的常量类
 */
public class GmallConfig {
    // Hbase的命名空间
    public static final String HABSE_SCHEMA = "GMALL0820_REALTIME";

    // Phonenix连接的服务器地址
    public static final String PHOENIX_SERVER = "jdbc:phoenix:hadoop202,hadoop203,hadoop204:2181";

    // ClickHouse的URL连接地址
    public static final String CLICKHOUSE_URL = "jdbc:clickhouse://hadoop202:8123/default";
}
