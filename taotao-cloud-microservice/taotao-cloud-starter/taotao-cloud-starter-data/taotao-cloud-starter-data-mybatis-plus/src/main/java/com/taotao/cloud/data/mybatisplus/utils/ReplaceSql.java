package com.taotao.cloud.data.mybatisplus.utils;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.alibaba.druid.sql.ast.expr.SQLExistsExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLCallStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.baomidou.mybatisplus.annotation.DbType;
import com.taotao.cloud.common.utils.log.LogUtils;

import java.util.List;

/**
 * 替换SQL
 */
public final class ReplaceSql {
    private ReplaceSql() {
    }

    public static String replaceSql(DbType dbType, String schemaName, String sql) {
        SQLStatementParser parser;
        switch (dbType) {
            case SQL_SERVER:
            case SQL_SERVER2005:
                parser = new SQLServerStatementParser(sql);
                break;
            default:
                parser = new MySqlStatementParser(sql);
                break;
        }

        SQLStatement sqlStatement = parser.parseStatement();
        if (sqlStatement instanceof SQLSelectStatement sqlSelectStatement) {
			SQLSelectQuery sqlSelectQuery = sqlSelectStatement.getSelect().getQuery();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlStatement instanceof SQLUpdateStatement sqlUpdateStatement) {
			SQLTableSource sqlTableSource = sqlUpdateStatement.getTableSource();
            setSqlSchemaBySqlTableSource(schemaName, sqlTableSource);
            SQLExpr where = sqlUpdateStatement.getWhere();
            setSqlSchemaBySqlExpr(schemaName, where);
        }
        if (sqlStatement instanceof SQLInsertStatement sqlInsertStatement) {
			SQLExprTableSource tableSource = sqlInsertStatement.getTableSource();
            setSqlSchemaBySqlTableSource(schemaName, tableSource);
        }
        if (sqlStatement instanceof SQLDeleteStatement sqlDeleteStatement) {
			SQLTableSource tableSource = sqlDeleteStatement.getTableSource();
            setSqlSchemaBySqlTableSource(schemaName, tableSource);
            SQLExpr where = sqlDeleteStatement.getWhere();
            setSqlSchemaBySqlExpr(schemaName, where);
        }
        if (sqlStatement instanceof SQLCreateTableStatement sqlCreateStatement) {
			SQLExprTableSource tableSource = sqlCreateStatement.getTableSource();
            setSqlSchemaBySqlTableSource(schemaName, tableSource);
        }
        if (sqlStatement instanceof SQLCallStatement sqlCallStatement) {
            LogUtils.info("执行到 存储过程 这里了");
			SQLName expr = sqlCallStatement.getProcedureName();
            if (expr instanceof SQLIdentifierExpr procedureName) {
				sqlCallStatement.setProcedureName(new SQLPropertyExpr(schemaName, procedureName.getName()));
            } else if (expr instanceof SQLPropertyExpr procedureName) {
				sqlCallStatement.setProcedureName(new SQLPropertyExpr(schemaName, procedureName.getName()));
            }
        }
        return sqlStatement.toString();
    }

    private static void setSqlSchemaBySqlTableSource(String schemaName, SQLTableSource sqlTableSource) {
        if (sqlTableSource instanceof SQLJoinTableSource sqlJoinTableSource) {
			SQLTableSource sqlTableSourceLeft = sqlJoinTableSource.getLeft();
            setSqlSchemaBySqlTableSource(schemaName, sqlTableSourceLeft);
            SQLTableSource sqlTableSourceRight = sqlJoinTableSource.getRight();
            setSqlSchemaBySqlTableSource(schemaName, sqlTableSourceRight);
            SQLExpr condition = sqlJoinTableSource.getCondition();
            setSqlSchemaBySqlExpr(schemaName, condition);
        }
        if (sqlTableSource instanceof SQLSubqueryTableSource sqlSubqueryTableSource) {
			SQLSelectQuery sqlSelectQuery = sqlSubqueryTableSource.getSelect().getQuery();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlTableSource instanceof SQLUnionQueryTableSource sqlUnionQueryTableSource) {
			SQLSelectQuery sqlSelectQueryLeft = sqlUnionQueryTableSource.getUnion().getLeft();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQueryLeft);
            SQLSelectQuery sqlSelectQueryRight = sqlUnionQueryTableSource.getUnion().getRight();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQueryRight);
        }
        if (sqlTableSource instanceof SQLExprTableSource sqlExprTableSource) {
			SQLObject sqlObject = sqlExprTableSource.getParent();

            if (sqlObject instanceof SQLDeleteStatement deleteStatement) {
				SQLExpr sqlExpr = deleteStatement.getWhere();
                setSqlSchemaBySqlExpr(schemaName, sqlExpr);
            }
            if (sqlObject instanceof SQLInsertStatement insertStatement) {
				SQLSelect sqlSelect = insertStatement.getQuery();
                if (sqlSelect != null) {
                    SQLSelectQuery sqlSelectQuery = sqlSelect.getQuery();
                    setSqlSchemaBySelectQuery(schemaName, sqlSelectQuery);
                }
            }
            sqlExprTableSource.setSchema(schemaName);
        }
    }

    private static void setSqlSchemaBySqlBinaryExpr(String schemaName, SQLBinaryOpExpr sqlBinaryOpExpr) {
        SQLExpr sqlExprLeft = sqlBinaryOpExpr.getLeft();
        setSqlSchemaBySqlExpr(schemaName, sqlExprLeft);
        SQLExpr sqlExprRight = sqlBinaryOpExpr.getRight();
        setSqlSchemaBySqlExpr(schemaName, sqlExprRight);
    }

    private static void setSqlSchemaBySqlExpr(String schemaName, SQLExpr sqlExpr) {
        if (sqlExpr instanceof SQLInSubQueryExpr sqlInSubQueryExpr) {
			SQLSelectQuery sqlSelectQuery = sqlInSubQueryExpr.getSubQuery().getQuery();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlExpr instanceof SQLExistsExpr sqlExistsExpr) {
			SQLSelectQuery sqlSelectQuery = sqlExistsExpr.getSubQuery().getQuery();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlExpr instanceof SQLCaseExpr sqlCaseExpr) {
			List<SQLCaseExpr.Item> sqlCaseExprItemList = sqlCaseExpr.getItems();
            for (SQLCaseExpr.Item item : sqlCaseExprItemList) {
                SQLExpr sqlExprItem = item.getValueExpr();
                setSqlSchemaBySqlExpr(schemaName, sqlExprItem);
            }
        }
        if (sqlExpr instanceof SQLQueryExpr sqlQueryExpr) {
			SQLSelectQuery sqlSelectQuery = sqlQueryExpr.getSubQuery().getQuery();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlExpr instanceof SQLBinaryOpExpr sqlBinaryOpExpr) {
			setSqlSchemaBySqlBinaryExpr(schemaName, sqlBinaryOpExpr);
        }
        if (sqlExpr instanceof SQLAggregateExpr sqlAggregateExpr) {
			List<SQLExpr> arguments = sqlAggregateExpr.getArguments();
            for (SQLExpr argument : arguments) {
                setSqlSchemaBySqlExpr(schemaName, argument);
            }
        }
    }

    private static void setSqlSchemaBySelectQuery(String schemaName, SQLSelectQuery sqlSelectQuery) {
        if (sqlSelectQuery instanceof SQLUnionQuery sqlUnionQuery) {
			SQLSelectQuery sqlSelectQueryLeft = sqlUnionQuery.getLeft();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQueryLeft);
            SQLSelectQuery sqlSelectQueryRight = sqlUnionQuery.getRight();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQueryRight);
        }
        if (sqlSelectQuery instanceof SQLSelectQueryBlock sqlSelectQueryBlock) {
			SQLTableSource sqlTableSource = sqlSelectQueryBlock.getFrom();
            setSqlSchemaBySqlTableSource(schemaName, sqlTableSource);
            SQLExpr whereSqlExpr = sqlSelectQueryBlock.getWhere();
            if (whereSqlExpr instanceof SQLInSubQueryExpr sqlInSubQueryExpr) {
				SQLSelectQuery sqlSelectQueryIn = sqlInSubQueryExpr.getSubQuery().getQuery();
                setSqlSchemaBySelectQuery(schemaName, sqlSelectQueryIn);
            }
            if (whereSqlExpr instanceof SQLBinaryOpExpr sqlBinaryOpExpr) {
				setSqlSchemaBySqlBinaryExpr(schemaName, sqlBinaryOpExpr);
            }
            List<SQLSelectItem> sqlSelectItemList = sqlSelectQueryBlock.getSelectList();
            for (SQLSelectItem sqlSelectItem : sqlSelectItemList) {
                SQLExpr sqlExpr = sqlSelectItem.getExpr();
                setSqlSchemaBySqlExpr(schemaName, sqlExpr);

                //函数
                if (sqlExpr instanceof SQLMethodInvokeExpr && ((SQLSelectQueryBlock) sqlSelectQuery).getFrom() == null) {
                    LogUtils.info("执行到 函数 这里了");
                    ((SQLMethodInvokeExpr) sqlExpr).setOwner(new SQLIdentifierExpr(schemaName));
                }
            }
        }
    }
}
