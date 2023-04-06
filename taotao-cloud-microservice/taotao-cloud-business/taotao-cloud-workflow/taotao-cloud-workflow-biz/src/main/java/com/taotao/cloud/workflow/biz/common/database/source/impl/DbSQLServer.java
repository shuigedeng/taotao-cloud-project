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
import com.taotao.cloud.workflow.biz.common.database.data.DataSourceContextHolder;
import com.taotao.cloud.workflow.biz.common.database.source.DbBase;
import com.taotao.cloud.workflow.biz.common.database.sql.impl.SqlSQLServer;
import java.sql.ResultSet;
import java.sql.SQLException;

/** SQLServer模型 */
public class DbSQLServer extends DbBase {

    @Override
    protected void init() {
        setInstance(
                DbBase.SQL_SERVER,
                DbType.SQL_SERVER,
                "1433",
                "sqlserver",
                "com.microsoft.sqlserver.jdbc.SQLServerDriver",
                "jdbc:sqlserver://{host}:{port};databaseName={dbname}",
                new SqlSQLServer(this));
    }

    @Override
    protected String getDynamicTableName(String tableName) {
        return DataSourceContextHolder.getDatasourceName() + ".dbo." + tableName;
    }

    @Override
    public DbTableFieldModel getPartFieldModel(ResultSet result) throws SQLException, DataException {
        DbTableFieldModel model = new DbTableFieldModel();
        /*  Text 和 Image 是可能被 SQServer 以后的版本淘汰的数据类型
        varchar(max)-------text;
        nvarchar(max)-----ntext;
        varbinary(max)----p_w_picpath.
        查询出来只能显示nvarchar，长度-1的时候代表nvarchar(max)，项目中转换成text */
        // 字段类型
        String dataType = result.getString(DbAliasEnum.getAsByDb(this, DbAliasConst.DATA_TYPE));
        // 字段长度
        int dataLength = result.getInt(DbAliasEnum.getAsByDb(this, DbAliasConst.DATA_LENGTH));
        // nvarchar会显示-1
        if ("nvarchar".equals(dataType) && dataLength == -1) {
            model.setDataType(DataTypeEnum.TEXT.getViewFieldType());
            model.setDefaults("默认");
        }
        return model;
    }
}
