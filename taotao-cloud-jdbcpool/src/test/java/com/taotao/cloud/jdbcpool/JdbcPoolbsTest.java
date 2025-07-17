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

package com.taotao.cloud.jdbcpool;

import com.taotao.cloud.jdbcpool.bs.JdbcPoolBs;
import javax.sql.DataSource;
import org.junit.jupiter.api.Test;

/**
 *
 * @author shuigedeng
 * @since 1.2.0
 */
public class JdbcPoolbsTest {

    @Test
    public void bsTest() {
        JdbcPoolBs jdbcPoolBs =
                JdbcPoolBs.newInstance()
                        .username("root")
                        .password("123456")
                        .url(
                                "jdbc:mysql://127.0.0.1:3306/test?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true");

        DataSource pooled = jdbcPoolBs.pooled();
        DataSource unPooled = jdbcPoolBs.unPooled();
    }
}
