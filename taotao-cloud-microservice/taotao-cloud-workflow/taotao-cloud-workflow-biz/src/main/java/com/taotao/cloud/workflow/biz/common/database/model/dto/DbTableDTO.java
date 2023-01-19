package com.taotao.cloud.workflow.biz.common.database.model.dto;

import java.sql.Connection;
import java.util.List;
import lombok.Data;

/**
 * 表数据传输对象
 *
 */
@Data
public class DbTableDTO {


    public DbTableDTO(Connection conn, DbTableModel dbTableModel, List<DbTableFieldModel> dbTableFieldList,String tableSpace){
        this.conn = conn;
        this.dbTableFieldList = dbTableFieldList;
        this.tableComment = dbTableModel.getTableComment();
        this.tableSpace = tableSpace;
    }

    /**
     * 主键改变标识
     */
    private Boolean priChangFlag = false;

    /**
     * 数据源
     */
    private Connection conn;

    /**==============数据库信息==============**/

    private String dbName;

    private String tableSpace;

    /**===============表信息=================**/

    /**
     * 查询时被使用表名
     */
    private String originTable;

    /**
     * 表说明
     */
    private String tableComment;


    /**===============字段信息=================**/

    /**
     * 字段信息集合
     */
    private List<DbTableFieldModel> dbTableFieldList;



}
