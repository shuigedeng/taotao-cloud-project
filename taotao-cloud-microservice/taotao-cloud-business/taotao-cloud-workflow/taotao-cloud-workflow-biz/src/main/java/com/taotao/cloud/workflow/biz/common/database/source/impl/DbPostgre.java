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

package com.taotao.cloud.workflow.biz.common.database.source.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.taotao.cloud.workflow.biz.common.database.source.DbBase;
import com.taotao.cloud.workflow.biz.common.database.sql.impl.SqlPostgre;
import java.sql.ResultSet;
import java.sql.SQLException;

/** PostgreSQL模型 */
public class DbPostgre extends DbBase {

    @Override
    protected void init() {
        setInstance(
                DbBase.POSTGRE_SQL,
                DbType.POSTGRE_SQL,
                "5432",
                "postgresql",
                "org.postgresql.Driver",
                "jdbc:postgresql://{host}:{port}/{dbname}",
                new SqlPostgre(this));
    }

    @Override
    protected String getDynamicTableName(String tableName) {
        return DataSourceContextHolder.getDatasourceName().toUpperCase() + "." + tableName;
    }

    @Override
    public DbTableFieldModel getPartFieldModel(ResultSet result) throws SQLException, DataException {
        DbTableFieldModel model = new DbTableFieldModel();
        // "t"不允许为空,"f"允许为空
        if (result.getString(DbAliasEnum.ALLOW_NULL.asByDb(this)).equals("t")) {
            model.setAllowNull(DbAliasEnum.ALLOW_NULL.isFalse());
        } else {
            model.setAllowNull(DbAliasEnum.ALLOW_NULL.isTrue());
        }
        return model;
    }
}
