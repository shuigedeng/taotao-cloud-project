package com.taotao.cloud.workflow.api.common.model.visiual;

import java.util.List;
import lombok.Data;

/**
 *
 */
@Data
public class TableModel {

     /**
     * 类型：1-主表、0-子表
     */
     private String typeId;
     /**
     * 表名
     */
     private String table;
     /**
     * 说明
     */
     private String tableName;
     /**
     * 主键
     */
     private String tableKey;
     /**
     * 外键字段
     */
     private String tableField;
     /**
     * 关联主表
     */
     private String relationTable;
     /**
     * 关联主键
     */
     private String relationField;

     private List<TableFields> fields;
}
