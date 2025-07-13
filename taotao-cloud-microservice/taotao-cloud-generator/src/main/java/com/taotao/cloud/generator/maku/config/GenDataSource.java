package com.taotao.cloud.generator.maku.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import com.taotao.cloud.generator.maku.config.DbType;
import com.taotao.cloud.generator.maku.config.query.*;
import com.taotao.cloud.generator.maku.entity.DataSourceEntity;
import com.taotao.cloud.generator.maku.utils.DbUtils;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 代码生成器 数据源
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
@Data
@Slf4j
public class GenDataSource {
    /**
     * 数据源ID
     */
    private Long id;
    /**
     * 数据库类型
     */
    private DbType dbType;
    /**
     * 数据库URL
     */
    private String connUrl;
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;

    private AbstractQuery dbQuery;

    private Connection connection;

    public GenDataSource(DataSourceEntity entity) {
        this.id = entity.getId();
        this.dbType = DbType.getValue(entity.getDbType());
        this.connUrl = entity.getConnUrl();
        this.username = entity.getUsername();
        this.password = entity.getPassword();

        if (dbType == DbType.MySQL) {
            this.dbQuery = new MySqlQuery();
        } else if (dbType == DbType.Oracle) {
            this.dbQuery = new OracleQuery();
        } else if (dbType == DbType.PostgreSQL) {
            this.dbQuery = new PostgreSqlQuery();
        } else if (dbType == DbType.SQLServer) {
            this.dbQuery = new SQLServerQuery();
        } else if (dbType == DbType.DM) {
            this.dbQuery = new DmQuery();
        } else if (dbType == DbType.Clickhouse) {
            this.dbQuery = new ClickHouseQuery();
        }else if (dbType == DbType.KingBase) {
            this.dbQuery = new KingBaseSqlQuery();
        }

        try {
            this.connection = DbUtils.getConnection(this);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public GenDataSource(Connection connection) throws SQLException {
        this.id = 0L;
        this.dbType = DbType.getValue(connection.getMetaData().getDatabaseProductName());

        if (dbType == DbType.MySQL) {
            this.dbQuery = new MySqlQuery();
        } else if (dbType == DbType.Oracle) {
            this.dbQuery = new OracleQuery();
        } else if (dbType == DbType.PostgreSQL) {
            this.dbQuery = new PostgreSqlQuery();
        } else if (dbType == DbType.SQLServer) {
            this.dbQuery = new SQLServerQuery();
        } else if (dbType == DbType.DM) {
            this.dbQuery = new DmQuery();
        } else if (dbType == DbType.Clickhouse) {
            this.dbQuery = new ClickHouseQuery();
        }else if (dbType == DbType.KingBase) {
            this.dbQuery = new KingBaseSqlQuery();
        }

        this.connection = connection;
    }
}
