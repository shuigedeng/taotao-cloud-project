package com.taotao.cloud.workflow.biz.common.database.model;

import lombok.Data;

/**
 * 类功能
 *
 */
@Data
public class DbTableInfoModel {

    /**
     * 表名
     */
    private String table;

    /**
     * 表类型
     */
    private String tableType;

    /**
     * 表注释
     */
    private String comment;

    /**
     * 主键字段
     */
    private String primaryField;


}
