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

package com.taotao.cloud.ai.alibaba.advisor.memory_mysql.config;

import com.alibaba.cloud.ai.memory.jdbc.MysqlChatMemoryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 * @author yingzi
 * @date 2025/5/28 08:12
 */
@Configuration
public class MysqlMemoryConfig {

    @Value("${spring.ai.chat.memory.repository.jdbc.mysql.jdbc-url}")
    private String mysqlJdbcUrl;

    @Value("${spring.ai.chat.memory.repository.jdbc.mysql.username}")
    private String mysqlUsername;

    @Value("${spring.ai.chat.memory.repository.jdbc.mysql.password}")
    private String mysqlPassword;

    @Value("${spring.ai.chat.memory.repository.jdbc.mysql.driver-class-name}")
    private String mysqlDriverClassName;

    @Bean
    public MysqlChatMemoryRepository mysqlChatMemoryRepository() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(mysqlDriverClassName);
        dataSource.setUrl(mysqlJdbcUrl);
        dataSource.setUsername(mysqlUsername);
        dataSource.setPassword(mysqlPassword);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return MysqlChatMemoryRepository.mysqlBuilder().jdbcTemplate(jdbcTemplate).build();
    }
}
