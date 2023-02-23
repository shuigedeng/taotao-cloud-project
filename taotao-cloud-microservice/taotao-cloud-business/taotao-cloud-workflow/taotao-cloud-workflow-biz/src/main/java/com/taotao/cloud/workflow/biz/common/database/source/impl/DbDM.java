package com.taotao.cloud.workflow.biz.common.database.source.impl;

import com.baomidou.mybatisplus.annotation.DbType;
import com.taotao.cloud.workflow.biz.common.database.source.DbBase;
import java.util.LinkedList;

/**
 * 达梦模型
 *
 * @date 2021/10/06
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
//                    e.printStackTrace();
//                }
//            });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static Integer getSum(Connection connection, String table) throws DataException {
//        String sql = "SELECT COUNT(*) as F_SUM FROM " + table;
//        return JdbcUtil.queryOneInt(connection, sql, "F_SUM");
//    }

}
