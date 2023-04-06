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

package com.taotao.cloud.workflow.biz.common.database.util;

import com.taotao.cloud.workflow.biz.common.database.model.dto.PreparedStatementDTO;
import java.util.List;

/** 类功能 */
public class SqlUtil {

    /*============独有方法=============*/
    /*
     * 为了便于快速定位，个别数据库独有方式，抽取成独有变量来解耦
     */
    /** MySQL、SQLServer独有注释方法 */
    public static List<PreparedStatementDTO> getMysqlSqlServerComments(CreateSqlDTO createSqlDTO) {
        // 执行一些个别数据独有的方式
        if (createSqlDTO.getDbBase().getClass() == DbSQLServer.class) {
            return SqlSQLServer.getTableComment(
                    createSqlDTO.getTableComment(), createSqlDTO.getNewTable(), createSqlDTO.getFieldModels());
        }
        return null;
    }

    /**
     * MySQL 字段注解方式
     *
     * @param fieldComment 字段注解
     * @return mysql字段注解
     */
    public static String getMysqlFieldComment(String fieldComment, DbBase dbBase) {
        if (dbBase.getClass() == DbMySQL.class) {
            return SqlMySQL.getCreFieldComment(fieldComment);
        }
        return "";
    }

    /**
     * MySQL独有,varchar作为主键的特殊处理
     *
     * @return
     */
    public static DataTypeModel getMysqlDataTypeModel(
            DbBase dbBase, Integer primaryKey, String dataType, DataTypeModel dataTypeModel) {
        if (dbBase.getClass() == DbMySQL.class) {
            if (primaryKey == 1 && dataType.equals(ViewDataTypeConst.VARCHAR)) {
                return DtMySQL.VARCHAR_KEY.getDataTypeModel();
            }
        }
        return dataTypeModel;
    }
}
