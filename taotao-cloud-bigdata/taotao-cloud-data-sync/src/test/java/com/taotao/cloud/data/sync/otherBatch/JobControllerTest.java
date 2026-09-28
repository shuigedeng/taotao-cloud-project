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

package com.taotao.cloud.data.sync.otherBatch;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

/**
 * <p>
 * JobControllerTest
 * </p>
 *
 * @author livk
 */
@SpringBootTest({
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.url=jdbc:h2:mem:test",
    "spring.sql.init.schema-locations=classpath*:/org/springframework/batch/core/schema-h2.sql",
    "spring.sql.init.platform=h2",
    "spring.sql.init.mode=embedded"
})
@AutoConfigureMockMvc
class JobControllerTest {

    @Autowired MockMvc mockMvc;

    @Autowired JdbcTemplate jdbcTemplate;

    @Test
    void doJobTest() throws Exception {
        mockMvc.perform(get("/doJob")).andExpect(status().isOk());
        List<String> names =
                jdbcTemplate.queryForList("select * from sys_user").stream()
                        .map(map -> map.get("user_name").toString())
                        .toList();
        Assertions.assertLinesMatch(List.of("张三", "李四", "王雪", "孙云", "赵柳", "孙雪"), names);
    }
}
