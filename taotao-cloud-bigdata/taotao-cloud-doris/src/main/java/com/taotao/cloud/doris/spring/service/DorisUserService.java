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

package com.taotao.cloud.doris.spring.service;

import com.taotao.cloud.clickhouse.mapper.CkUserMapper;
import com.taotao.cloud.clickhouse.model.CkUser;
import com.taotao.cloud.datasource.ck.ClickHouseJdbcBaseDaoImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DorisUserService extends ClickHouseJdbcBaseDaoImpl {

    @Autowired
    private CkUserMapper userMapper;

    public void testUseJdbcTemplate() {
        getJdbcTemplate().query("select * from user", rs -> {
            LogUtils.info(rs);
        });
    }

    public List testUseMapperInterface() {
        List userList = userMapper.queryUser();

        CkUser user = new CkUser();
        Integer flag = userMapper.insertUser(user);

        LogUtils.info("dslfkajsldflsdfk");
        return userList;
    }
}