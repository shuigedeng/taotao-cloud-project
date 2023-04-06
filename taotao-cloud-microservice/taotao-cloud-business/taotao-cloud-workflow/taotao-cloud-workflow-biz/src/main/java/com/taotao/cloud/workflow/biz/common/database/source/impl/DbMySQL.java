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
import java.sql.ResultSet;

/** MySQL模型 */
public class DbMySQL extends DbBase {

    @Override
    protected void init() {
        setInstance(
                DbBase.MYSQL,
                DbType.MYSQL,
                "3306",
                "mysql",
                "com.mysql.cj.jdbc.Driver",
                "jdbc:mysql://{host}:{port}/{dbname}?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&serverTimezone=GMT%2B8&useSSL=false",
                // connUrl =
                // "jdbc:mysql://{host}:{port}/{dbname}?useUnicode=true&autoReconnect=true&characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8";
                new SqlMySQL(this));
    }

    @Override
    public DbTableFieldModel getPartFieldModel(ResultSet result) throws Exception {
        DbTableFieldModel model = new DbTableFieldModel();
        // 精度
        String precision = result.getString(DbAliasConst.PRECISION);
        DataTypeModel dataTypeModel =
                getDataTypeModel(result.getString(DbAliasEnum.getAsByDb(this, DbAliasConst.DATA_TYPE)));
        if (dataTypeModel != null) {
            if (!StringUtil.isEmpty(precision) || dataTypeModel.getPrecisionMax() != null) {
                model.setDataLength(precision + "," + result.getString(DbAliasConst.DECIMALS));
            }
        }
        return model;
    }
}
