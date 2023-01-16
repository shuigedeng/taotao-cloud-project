package com.taotao.cloud.workflow.api.common.database.model.dto;

import com.taotao.cloud.workflow.api.common.base.NoDataSourceBind;
import java.sql.Connection;
import java.util.Arrays;
import java.util.LinkedList;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * SQL参数传输对象
 *
 */

@Data
@NoDataSourceBind
@AllArgsConstructor
public class PreparedStatementDTO {
    /**
     * 执行的sql语句用占位符代替
     */
    private String prepareSql;
    /**
     * conn连接
     */
    private Connection conn;
    /**
     * sql对应占位符的值
     */
    private LinkedList<Object> prepareDataList;
    /**
     * 是否是系统语句
     */
    private Boolean systemSql = false;

    /**
     * 是否显示别名
     */
    private Boolean isAlias;

    public PreparedStatementDTO(Connection conn, String prepareSql){
        this.conn = conn;
        this.prepareSql = prepareSql;
        this.prepareDataList =new LinkedList<>();
    }

    public PreparedStatementDTO(Connection conn, String prepareSql, Object ...objs){
        this.conn = conn;
        this.prepareSql = prepareSql;
        this.prepareDataList = new LinkedList<>(Arrays.asList(objs));
    }

    public PreparedStatementDTO(Connection conn, String prepareSql, LinkedList<Object> prepareDataList){
        this.conn = conn;
        this.prepareSql = prepareSql;
        this.prepareDataList = prepareDataList;
    }

}
