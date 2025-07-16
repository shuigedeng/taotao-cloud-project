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

package com.taotao.cloud.flink.cep;

public final class Constants {
    // Required configurations constants for connecting to Database
    public static final String JDBC_URL_ARG = "jdbcUrl";
    public static final String JDBC_DRIVE = "com.mysql.cj.jdbc.Driver";
    public static final String TABLE_NAME_ARG = "tableName";
    public static final String JDBC_INTERVAL_MILLIS_ARG = "jdbcIntervalMs";
    // Required configurations constants for connecting to Kafka
    public static final String KAFKA_BROKERS_ARG = "kafkaBrokers";
    public static final String INPUT_TOPIC_ARG = "inputTopic";
    public static final String INPUT_TOPIC_GROUP_ARG = "inputTopicGroup";
}
