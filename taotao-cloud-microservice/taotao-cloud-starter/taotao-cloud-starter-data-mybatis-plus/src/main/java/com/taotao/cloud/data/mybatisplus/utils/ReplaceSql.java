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
        if (sqlStatement instanceof SQLSelectStatement) {
            SQLSelectStatement sqlSelectStatement = (SQLSelectStatement) sqlStatement;
            SQLSelectQuery sqlSelectQuery = sqlSelectStatement.getSelect().getQuery();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlStatement instanceof SQLUpdateStatement) {
            SQLUpdateStatement sqlUpdateStatement = (SQLUpdateStatement) sqlStatement;
            SQLTableSource sqlTableSource = sqlUpdateStatement.getTableSource();
            setSqlSchemaBySqlTableSource(schemaName, sqlTableSource);
            SQLExpr where = sqlUpdateStatement.getWhere();
            setSqlSchemaBySqlExpr(schemaName, where);
        }
        if (sqlStatement instanceof SQLInsertStatement) {
            SQLInsertStatement sqlInsertStatement = (SQLInsertStatement) sqlStatement;
            SQLExprTableSource tableSource = sqlInsertStatement.getTableSource();
            setSqlSchemaBySqlTableSource(schemaName, tableSource);
        }
        if (sqlStatement instanceof SQLDeleteStatement) {
            SQLDeleteStatement sqlDeleteStatement = (SQLDeleteStatement) sqlStatement;
            SQLTableSource tableSource = sqlDeleteStatement.getTableSource();
            setSqlSchemaBySqlTableSource(schemaName, tableSource);
            SQLExpr where = sqlDeleteStatement.getWhere();
            setSqlSchemaBySqlExpr(schemaName, where);
        }
        if (sqlStatement instanceof SQLCreateTableStatement) {
            SQLCreateTableStatement sqlCreateStatement = (SQLCreateTableStatement) sqlStatement;
            SQLExprTableSource tableSource = sqlCreateStatement.getTableSource();
            setSqlSchemaBySqlTableSource(schemaName, tableSource);
        }
        if (sqlStatement instanceof SQLCallStatement) {
            log.info("执行到 存储过程 这里了");
            SQLCallStatement sqlCallStatement = (SQLCallStatement) sqlStatement;
            SQLName expr = sqlCallStatement.getProcedureName();
            if (expr instanceof SQLIdentifierExpr) {
                SQLIdentifierExpr procedureName = (SQLIdentifierExpr) expr;
                sqlCallStatement.setProcedureName(new SQLPropertyExpr(schemaName, procedureName.getName()));
            } else if (expr instanceof SQLPropertyExpr) {
                SQLPropertyExpr procedureName = (SQLPropertyExpr) expr;
                sqlCallStatement.setProcedureName(new SQLPropertyExpr(schemaName, procedureName.getName()));
            }
        }
        return sqlStatement.toString();
    }

    private static void setSqlSchemaBySqlTableSource(String schemaName, SQLTableSource sqlTableSource) {
        if (sqlTableSource instanceof SQLJoinTableSource) {
            SQLJoinTableSource sqlJoinTableSource = (SQLJoinTableSource) sqlTableSource;
            SQLTableSource sqlTableSourceLeft = sqlJoinTableSource.getLeft();
            setSqlSchemaBySqlTableSource(schemaName, sqlTableSourceLeft);
            SQLTableSource sqlTableSourceRight = sqlJoinTableSource.getRight();
            setSqlSchemaBySqlTableSource(schemaName, sqlTableSourceRight);
            SQLExpr condition = sqlJoinTableSource.getCondition();
            setSqlSchemaBySqlExpr(schemaName, condition);
        }
        if (sqlTableSource instanceof SQLSubqueryTableSource) {
            SQLSubqueryTableSource sqlSubqueryTableSource = (SQLSubqueryTableSource) sqlTableSource;
            SQLSelectQuery sqlSelectQuery = sqlSubqueryTableSource.getSelect().getQuery();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlTableSource instanceof SQLUnionQueryTableSource) {
            SQLUnionQueryTableSource sqlUnionQueryTableSource = (SQLUnionQueryTableSource) sqlTableSource;
            SQLSelectQuery sqlSelectQueryLeft = sqlUnionQueryTableSource.getUnion().getLeft();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQueryLeft);
            SQLSelectQuery sqlSelectQueryRight = sqlUnionQueryTableSource.getUnion().getRight();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQueryRight);
        }
        if (sqlTableSource instanceof SQLExprTableSource) {
            SQLExprTableSource sqlExprTableSource = (SQLExprTableSource) sqlTableSource;
            SQLObject sqlObject = sqlExprTableSource.getParent();

            if (sqlObject instanceof SQLDeleteStatement) {
                SQLDeleteStatement deleteStatement = (SQLDeleteStatement) sqlObject;
                SQLExpr sqlExpr = deleteStatement.getWhere();
                setSqlSchemaBySqlExpr(schemaName, sqlExpr);
            }
            if (sqlObject instanceof SQLInsertStatement) {
                SQLInsertStatement insertStatement = (SQLInsertStatement) sqlObject;
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
        if (sqlExpr instanceof SQLInSubQueryExpr) {
            SQLInSubQueryExpr sqlInSubQueryExpr = (SQLInSubQueryExpr) sqlExpr;
            SQLSelectQuery sqlSelectQuery = sqlInSubQueryExpr.getSubQuery().getQuery();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlExpr instanceof SQLExistsExpr) {
            SQLExistsExpr sqlExistsExpr = (SQLExistsExpr) sqlExpr;
            SQLSelectQuery sqlSelectQuery = sqlExistsExpr.getSubQuery().getQuery();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlExpr instanceof SQLCaseExpr) {
            SQLCaseExpr sqlCaseExpr = (SQLCaseExpr) sqlExpr;
            List<SQLCaseExpr.Item> sqlCaseExprItemList = sqlCaseExpr.getItems();
            for (SQLCaseExpr.Item item : sqlCaseExprItemList) {
                SQLExpr sqlExprItem = item.getValueExpr();
                setSqlSchemaBySqlExpr(schemaName, sqlExprItem);
            }
        }
        if (sqlExpr instanceof SQLQueryExpr) {
            SQLQueryExpr sqlQueryExpr = (SQLQueryExpr) sqlExpr;
            SQLSelectQuery sqlSelectQuery = sqlQueryExpr.getSubQuery().getQuery();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQuery);
        }
        if (sqlExpr instanceof SQLBinaryOpExpr) {
            SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) sqlExpr;
            setSqlSchemaBySqlBinaryExpr(schemaName, sqlBinaryOpExpr);
        }
        if (sqlExpr instanceof SQLAggregateExpr) {
            SQLAggregateExpr sqlAggregateExpr = (SQLAggregateExpr) sqlExpr;
            List<SQLExpr> arguments = sqlAggregateExpr.getArguments();
            for (SQLExpr argument : arguments) {
                setSqlSchemaBySqlExpr(schemaName, argument);
            }
        }
    }

    private static void setSqlSchemaBySelectQuery(String schemaName, SQLSelectQuery sqlSelectQuery) {
        if (sqlSelectQuery instanceof SQLUnionQuery) {
            SQLUnionQuery sqlUnionQuery = (SQLUnionQuery) sqlSelectQuery;
            SQLSelectQuery sqlSelectQueryLeft = sqlUnionQuery.getLeft();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQueryLeft);
            SQLSelectQuery sqlSelectQueryRight = sqlUnionQuery.getRight();
            setSqlSchemaBySelectQuery(schemaName, sqlSelectQueryRight);
        }
        if (sqlSelectQuery instanceof SQLSelectQueryBlock) {
            SQLSelectQueryBlock sqlSelectQueryBlock = (SQLSelectQueryBlock) sqlSelectQuery;
            SQLTableSource sqlTableSource = sqlSelectQueryBlock.getFrom();
            setSqlSchemaBySqlTableSource(schemaName, sqlTableSource);
            SQLExpr whereSqlExpr = sqlSelectQueryBlock.getWhere();
            if (whereSqlExpr instanceof SQLInSubQueryExpr) {
                SQLInSubQueryExpr sqlInSubQueryExpr = (SQLInSubQueryExpr) whereSqlExpr;
                SQLSelectQuery sqlSelectQueryIn = sqlInSubQueryExpr.getSubQuery().getQuery();
                setSqlSchemaBySelectQuery(schemaName, sqlSelectQueryIn);
            }
            if (whereSqlExpr instanceof SQLBinaryOpExpr) {
                SQLBinaryOpExpr sqlBinaryOpExpr = (SQLBinaryOpExpr) whereSqlExpr;
                setSqlSchemaBySqlBinaryExpr(schemaName, sqlBinaryOpExpr);
            }
            List<SQLSelectItem> sqlSelectItemList = sqlSelectQueryBlock.getSelectList();
            for (SQLSelectItem sqlSelectItem : sqlSelectItemList) {
                SQLExpr sqlExpr = sqlSelectItem.getExpr();
                setSqlSchemaBySqlExpr(schemaName, sqlExpr);

                //函数
                if (sqlExpr instanceof SQLMethodInvokeExpr && sqlSelectQuery instanceof SQLSelectQueryBlock && ((SQLSelectQueryBlock) sqlSelectQuery).getFrom() == null) {
                    log.info("执行到 函数 这里了");
                    ((SQLMethodInvokeExpr) sqlExpr).setOwner(new SQLIdentifierExpr(schemaName));
                }
            }
        }
    }
}
