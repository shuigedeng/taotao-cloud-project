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

package com.taotao.cloud.workflow.biz.common.database.sql.append.insert;

import java.util.List;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/** 插入数据SQL模板 */
@Data
public class InsertSql {

    @Autowired
    DataSourceUtil dataSourceUtil;

    /*==================框架======================**/
    /*
     * 框架 - 基础
     * MySQL基本框架：INSERT INTO + {表名} + VALUES ({字段集合}),... + {注释}
     * Oracle基本框架：INSERT INTO + {表名} + VALUES ({字段集合}) + {注释}; ...
     * @return SQL
     */
    /*=================================================================**/

    /** 批量插入数据 使用不指定字段名SQL语句 */
    public static String batch(InsertSqlDTO insertSqlDTO) {
        StringBuilder sql = new StringBuilder();
        // 遍历游标
        for (List<DbFieldMod> data : insertSqlDTO.getDataList()) {
            StringBuilder values = new StringBuilder();
            // 遍历字段
            for (DbFieldMod field : data) {
                String fieldValue = insertSqlDTO.getFieldValue(field.getColumnTypeName(), field.getColumnValue());
                values.append(fieldValue);
            }
            String sqlFragment = insertSqlDTO.getOracleInsertBasicSql()
                    + "("
                    + values.substring(0, values.length() - 1)
                    + ")"
                    + insertSqlDTO.getBatchInsertSeparator();
            sql.append(sqlFragment);
        }
        return insertSqlDTO.getMysqlInsertBasicSql() + sql;
    }
}
