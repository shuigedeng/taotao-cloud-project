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
import java.util.LinkedList;

/**
 * 达梦模型
 *
 * @since 2021/10/06
 */
public class DbDM extends DbBase {

    @Override
    protected void init() {
        setInstance(
                DbBase.DM,
                DbType.DM,
                "5236",
                "dm",
                "dm.jdbc.driver.DmDriver",
                "jdbc:dm://{host}:{port}/{schema}",
                new SqlDM(this));
    }

    @Override
    public LinkedList<Object> getStructParams(String structParams, String table, DataSourceMod dbSourceOrDbLink) {
        DataSourceDTO dataSourceDTO = dbSourceOrDbLink.convertDTO();
        dataSourceDTO.setDbSchema(dataSourceDTO.getUserName());
        return super.getStructParams(structParams, table, dataSourceDTO);
    }

    //    public static void setDmTableModel(DbConnDTO connDTO, List<DbTableModel> tableModelList) {
    //        //达梦特殊方法
    //        try {
    //            @Cleanup Connection dmConn = connDTO.getConn();
    //            tableModelList.forEach(tm -> {
    //                try {
    //                    Integer sum = DbDM.getSum(dmConn, tm.getTable());
    //                    tm.setSum(sum);
    //                } catch (DataException e) {
    //                    LogUtils.error(e);
    //                }
    //            });
    //        } catch (Exception e) {
    //            LogUtils.error(e);
    //        }
    //    }
    //
    //    private static Integer getSum(Connection connection, String table) throws DataException {
    //        String sql = "SELECT COUNT(*) as F_SUM FROM " + table;
    //        return JdbcUtil.queryOneInt(connection, sql, "F_SUM");
    //    }

}
