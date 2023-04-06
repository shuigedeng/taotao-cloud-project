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

package com.taotao.cloud.data.analysis.datasource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = DataSourceProperties.DS, ignoreUnknownFields = false)
@Data
public class DataSourceProperties {
    static final String DS = "spring.datasource";

    private Mysql mysql;
    private ClickHouse clickhouse;
    private Doris doris;
    private Hive hive;
    private Tidb tidb;
    private Trino trino;

    @Data
    public static class Mysql {
        private boolean enabled = false;

        private String driverClassName;
        private String url;
        private String username;
        private String password;
    }

    @Data
    public static class Trino {
        private boolean enabled = false;

        private String driverClassName;
        private String url;
        private String username;
        private String password;
    }

    @Data
    public static class Tidb {
        private boolean enabled = false;

        private String driverClassName;
        private String url;
        private String username;
        private String password;
    }

    @Data
    public static class Hive {
        private boolean enabled = false;

        private String driverClassName;
        private String url;
        private String username;
        private String password;
    }

    @Data
    public static class Doris {
        private boolean enabled = false;

        private String driverClassName;
        private String url;
        private String username;
        private String password;
    }

    @Data
    public static class ClickHouse {
        private boolean enabled = false;

        private String driverClassName;
        private String url;
        private String username;
        private String password;
    }
}
