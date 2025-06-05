
package com.taotao.cloud.generator.maku.config.query;

import cn.hutool.core.util.StrUtil;
import com.taotao.cloud.generator.maku.config.DbType;
import com.taotao.cloud.generator.maku.config.query.AbstractQuery;

/**
 * ClickHouse 表数据查询
 *
 * @author ratelfu
 * @since 2021-03-10
 */
public class ClickHouseQuery implements AbstractQuery {


    @Override
    public String tableFieldsSql() {
        return "select * from system.columns where table='%s'";
    }

    @Override
    public DbType dbType() {
        return DbType.Clickhouse;
    }

    @Override
    public String tableSql(String tableName) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM system.tables WHERE 1=1 ");

        // 表名查询
        if (StrUtil.isNotBlank(tableName)) {
            sql.append("and name = '").append(tableName).append("' ");
        }
        return sql.toString();
    }

    @Override
    public String tableName() {
        return "name";
    }

    @Override
    public String tableComment() {
        return "comment";
    }

    @Override
    public String fieldName() {
        return "name";
    }


    @Override
    public String fieldType() {
        return "type";
    }


    @Override
    public String fieldComment() {
        return "comment";
    }


    @Override
    public String fieldKey() {
        return "is_in_primary_key";
    }



}
