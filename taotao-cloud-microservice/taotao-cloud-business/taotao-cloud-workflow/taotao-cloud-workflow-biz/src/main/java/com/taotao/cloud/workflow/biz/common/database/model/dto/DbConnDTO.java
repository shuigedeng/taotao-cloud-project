package com.taotao.cloud.workflow.biz.common.database.model.dto;

import java.sql.Connection;
import lombok.Data;

/**
 * 数据连接相关数据传输对象
 *
 */
@Data
public class DbConnDTO {

    public DbConnDTO(DbBase dbBase, DataSourceUtil dbSource, Connection conn){
        this.dbBase = dbBase;
        this.dbSource = dbSource;
        this.conn = conn;
    }

    /**
     * 数据库基类
     */
    private DbBase dbBase;

    /**
     * 数据源信息
     */
    private DataSourceUtil dbSource;

    /**
     * 数据连接
     */
    private Connection conn;
}
