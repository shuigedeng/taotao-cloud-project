package com.taotao.cloud.workflow.api.common.database.sql.append.create;

import java.util.List;
import lombok.Data;

/**
 * 类功能
 *
 */
@Data
public class CreateSqlDTO {


    public CreateSqlDTO(DbBase dbBase, String newTable, String tableComment, List<DbTableFieldModel> fieldModels){
        this.dbBase = dbBase;
        this.newTable = newTable;
        this.tableComment = tableComment;
        this.fieldModels = fieldModels;
    }

    /**
     * 数据库基类
     */
    private DbBase dbBase;

    /**
     * 新建表名
     */
    private String newTable;

    /**
     * 新建表注释
     */
    private String tableComment;

    /**
     * 新建表字段
     */
    private List<DbTableFieldModel> fieldModels;




}
