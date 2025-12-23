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

package com.taotao.cloud.data.analysis.tidb.service;

import com.taotao.boot.common.utils.log.LogUtils;
import com.taotao.boot.data.datasource.multiple.ck.ClickHouseJdbcBaseDaoImpl;
import com.taotao.cloud.data.analysis.clickhouse.mapper.CkUserMapper;
import com.taotao.cloud.data.analysis.clickhouse.model.CkUser;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TidbUserService extends ClickHouseJdbcBaseDaoImpl {

    @Autowired private CkUserMapper userMapper;

    public void testUseJdbcTemplate() {
        getJdbcTemplate()
                .query(
                        "select * from user",
                        rs -> {
                            LogUtils.info(rs.toString());
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
