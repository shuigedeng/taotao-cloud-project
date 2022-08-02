package com.taotao.cloud.sys.biz.modules.database.service.sqlparser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jdk.nashorn.internal.objects.annotations.Where;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import net.sf.jsqlparser.expression.*;
import net.sf.jsqlparser.expression.operators.arithmetic.*;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.*;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SubSelect;

@Slf4j
public class WhereTableColumnFinder implements ExpressionVisitor {
    private Map<String,ExtendFindTable> tableAliasMap = new HashMap<>();
    private MultiValueMap<String, com.sanri.tools.modules.database.service.meta.dtos.Column> tableColumnsMap = new LinkedMultiValueMap<>();

    public WhereTableColumnFinder(Map<String, ExtendFindTable> tableAliasMap) {
        this.tableAliasMap = tableAliasMap;
    }

    public MultiValueMap<String, com.sanri.tools.modules.database.service.meta.dtos.Column> getTableColumnsMap(Expression where){
        where.accept(this);

        return tableColumnsMap;
    }

    @Override
    public void visit(NullValue nullValue) {

    }

    @Override
    public void visit(Function function) {
        // TODO 函数不好处理, 写 sql 生成代码最好别写函数
    }

    @Override
    public void visit(SignedExpression signedExpression) {

    }

    @Override
    public void visit(JdbcParameter jdbcParameter) {

    }

    @Override
    public void visit(JdbcNamedParameter jdbcNamedParameter) {

    }

    @Override
    public void visit(DoubleValue doubleValue) {

    }

    @Override
    public void visit(LongValue longValue) {

    }

    @Override
    public void visit(HexValue hexValue) {

    }

    @Override
    public void visit(DateValue dateValue) {

    }

    @Override
    public void visit(TimeValue timeValue) {

    }

    @Override
    public void visit(TimestampValue timestampValue) {

    }

    @Override
    public void visit(Parenthesis parenthesis) {

    }

    @Override
    public void visit(StringValue stringValue) {

    }

    @Override
    public void visit(Addition addition) {

    }

    @Override
    public void visit(Division division) {

    }

    @Override
    public void visit(Multiplication multiplication) {

    }

    @Override
    public void visit(Subtraction subtraction) {

    }

    @Override
    public void visit(AndExpression andExpression) {
        andExpression.getLeftExpression().accept(this);
        andExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(OrExpression orExpression) {
        orExpression.getLeftExpression().accept(this);
        orExpression.getRightExpression().accept(this);
    }

    @Override
    public void visit(Between between) {
        between.getLeftExpression().accept(this);
    }

    @Override
    public void visit(EqualsTo equalsTo) {
        equalsTo.getLeftExpression().accept(this);
    }

    @Override
    public void visit(GreaterThan greaterThan) {
        greaterThan.getLeftExpression().accept(this);
    }

    @Override
    public void visit(GreaterThanEquals greaterThanEquals) {
        greaterThanEquals.getLeftExpression().accept(this);
    }

    @Override
    public void visit(InExpression inExpression) {
        inExpression.getLeftExpression().accept(this);
    }

    @Override
    public void visit(IsNullExpression isNullExpression) {

    }

    @Override
    public void visit(LikeExpression likeExpression) {
        likeExpression.getLeftExpression().accept(this);
    }

    @Override
    public void visit(MinorThan minorThan) {
        minorThan.getLeftExpression().accept(this);
    }

    @Override
    public void visit(MinorThanEquals minorThanEquals) {
        minorThanEquals.getLeftExpression().accept(this);
    }

    @Override
    public void visit(NotEqualsTo notEqualsTo) {
        notEqualsTo.getLeftExpression().accept(this);
    }

    @Override
    public void visit(Column tableColumn) {
        final Table table = tableColumn.getTable();
        // 如果只有一张表, 则这个列肯定属于这张表, 与别名无关
        if (tableAliasMap.size() == 1){
            final ExtendFindTable extendFindTable = tableAliasMap.values().iterator().next();
            com.sanri.tools.modules.database.service.meta.dtos.Column findColumn = findTableColumn(tableColumn, extendFindTable);
            if (findColumn == null) {
                return;
            }
            tableColumnsMap.add(extendFindTable.getFindTable().getName(),findColumn);
            return ;
        }
        if (StringUtils.isNotBlank(table.getName())) {
            final ExtendFindTable extendFindTable = tableAliasMap.get(table.getName());
            if (extendFindTable != null ){
                log.error("没有找到别名: {}",table.getName());
                return ;
            }
            com.sanri.tools.modules.database.service.meta.dtos.Column findColumn = findTableColumn(tableColumn, extendFindTable);
            if (findColumn == null) {
                return;
            }
            tableColumnsMap.add(extendFindTable.getFindTable().getName(),findColumn);
        }else{
            // 如果没有别名, 则需要一张表一张表的查找
            final Iterator<ExtendFindTable> iterator = tableAliasMap.values().iterator();
            A:while (iterator.hasNext()){
                final ExtendFindTable extendFindTable = iterator.next();
                for (com.sanri.tools.modules.database.service.meta.dtos.Column column : extendFindTable.getTableMeta().getColumns()) {
                    if (column.getColumnName().equalsIgnoreCase(column.getColumnName())){
                        tableColumnsMap.add(extendFindTable.getFindTable().getName(),column);
                        break A;
                    }
                }
            }
        }
    }

    public com.sanri.tools.modules.database.service.meta.dtos.Column findTableColumn(Column tableColumn, ExtendFindTable extendFindTable) {
        com.sanri.tools.modules.database.service.meta.dtos.Column findColumn = null;
        for (com.sanri.tools.modules.database.service.meta.dtos.Column column : extendFindTable.getTableMeta().getColumns()) {
            if (column.getColumnName().equalsIgnoreCase(tableColumn.getColumnName())){
                findColumn = column;
                break;
            }
        }
        if (findColumn == null){
            log.warn("字段信息[{}]丢失,在数据表:{}", tableColumn.getColumnName(), extendFindTable.getFindTable().getName());
            return null;
        }
        return findColumn;
    }

    @Override
    public void visit(SubSelect subSelect) {

    }

    @Override
    public void visit(CaseExpression caseExpression) {

    }

    @Override
    public void visit(WhenClause whenClause) {

    }

    @Override
    public void visit(ExistsExpression existsExpression) {
    }

    @Override
    public void visit(AllComparisonExpression allComparisonExpression) {

    }

    @Override
    public void visit(AnyComparisonExpression anyComparisonExpression) {

    }

    @Override
    public void visit(Concat concat) {

    }

    @Override
    public void visit(Matches matches) {

    }

    @Override
    public void visit(BitwiseAnd bitwiseAnd) {

    }

    @Override
    public void visit(BitwiseOr bitwiseOr) {

    }

    @Override
    public void visit(BitwiseXor bitwiseXor) {

    }

    @Override
    public void visit(CastExpression cast) {

    }

    @Override
    public void visit(Modulo modulo) {

    }

    @Override
    public void visit(AnalyticExpression aexpr) {

    }

    @Override
    public void visit(WithinGroupExpression wgexpr) {

    }

    @Override
    public void visit(ExtractExpression eexpr) {

    }

    @Override
    public void visit(IntervalExpression iexpr) {

    }

    @Override
    public void visit(OracleHierarchicalExpression oexpr) {

    }

    @Override
    public void visit(RegExpMatchOperator rexpr) {

    }

    @Override
    public void visit(JsonExpression jsonExpr) {

    }

    @Override
    public void visit(RegExpMySQLOperator regExpMySQLOperator) {

    }

    @Override
    public void visit(UserVariable var) {

    }

    @Override
    public void visit(NumericBind bind) {

    }

    @Override
    public void visit(KeepExpression aexpr) {

    }

    @Override
    public void visit(MySQLGroupConcat groupConcat) {

    }

    @Override
    public void visit(RowConstructor rowConstructor) {

    }

    @Override
    public void visit(OracleHint hint) {

    }
}
