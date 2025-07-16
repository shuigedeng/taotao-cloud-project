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

package com.taotao.cloud.generator.maku.config.query;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.generator.maku.config.DbType;

/**
 * ClickHouse 表数据查询
 *
 * @author ratelfu
 * @since 2021-03-10
 */
public class ClickHouseQuery implements AbstractQuery {

    @Override
    public String tableFieldsSql() {
        return "select * from system.columns where table='%s'";
    }

    @Override
    public DbType dbType() {
        return DbType.Clickhouse;
    }

    @Override
    public String tableSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM system.tables WHERE 1=1 ");

        // 表名查询
        if (StrUtil.isNotBlank(tableName)) {
            sql.append("and name = '").append(tableName).append("' ");
        }
        return sql.toString();
    }

    @Override
    public String tableName() {
        return "name";
    }

    @Override
    public String tableComment() {
        return "comment";
    }

    @Override
    public String fieldName() {
        return "name";
    }

    @Override
    public String fieldType() {
        return "type";
    }

    @Override
    public String fieldComment() {
        return "comment";
    }

    @Override
    public String fieldKey() {
        return "is_in_primary_key";
    }
}
