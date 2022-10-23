package com.taotao.cloud.data.mybatisplus.datascope.perm.scope;

import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.parser.JsqlParserSupport;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.data.mybatisplus.datascope.perm.NestedPermission;
import com.taotao.cloud.data.mybatisplus.datascope.perm.Permission;
import com.taotao.cloud.data.mybatisplus.datascope.perm.code.DataScopeEnum;
import com.taotao.cloud.data.mybatisplus.datascope.perm.configuration.DataPermProperties;
import com.taotao.cloud.data.mybatisplus.datascope.perm.local.DataPermContextHolder;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectBody;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SetOperationList;
import net.sf.jsqlparser.statement.select.SubSelect;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
* 数据权限处理器
*/
@Component
public class DataScopeInterceptor extends JsqlParserSupport implements InnerInterceptor {
	public static final String CREATOR = "create_by";

    private final DataPermProperties dataPermProperties;
    private final DataPermScopeHandler dataPermScopeHandler;

	public DataScopeInterceptor(DataPermProperties dataPermProperties, DataPermScopeHandler dataPermScopeHandler) {
		this.dataPermProperties = dataPermProperties;
		this.dataPermScopeHandler = dataPermScopeHandler;
	}

	@Override
    public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds, ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
        // 配置是否开启了权限控制
        if (!dataPermProperties.isEnableDataPerm()){
            return;
        }
        // 判断是否在嵌套执行环境中
        NestedPermission nestedPermission = DataPermContextHolder.getNestedPermission();
        if (Objects.nonNull(nestedPermission) && !nestedPermission.dataScope()){
            return;
        }
        // 是否添加了对应的注解来开启数据权限控制
        Permission permission = DataPermContextHolder.getPermission();
        if (Objects.isNull(permission) || !permission.dataScope()){
            return;
        }
        // 检查是否已经登录和是否是超级管理员
        boolean admin = DataPermContextHolder.getUserDetail()
			.map(SecurityUser::getAdmin)
			.orElseThrow(() -> {
				throw new BusinessException("用户未登录");
			});
        // 是否超级管理员
        if (admin){
            return;
        }
        PluginUtils.MPBoundSql mpBs = PluginUtils.mpBoundSql(boundSql);
        mpBs.sql(this.parserSingle(mpBs.sql(), ms.getId()));
    }

    /**
     * 查询处理
     */
    @Override
    protected void processSelect(Select select, int index, String sql, Object obj) {
        SelectBody selectBody = select.getSelectBody();
        if (selectBody instanceof PlainSelect) {
            this.setWhere((PlainSelect) selectBody);
        } else if (selectBody instanceof SetOperationList setOperationList) {
			List<SelectBody> selectBodyList = setOperationList.getSelects();
            selectBodyList.forEach(s -> this.setWhere((PlainSelect) s));
        }
    }

    /**
     * 设置 where 条件
     *
     * @param plainSelect  查询对象
     */
    protected void setWhere(PlainSelect plainSelect) {
        Expression sqlSegment = this.dataScope(plainSelect.getWhere());
        if (null != sqlSegment) {
            plainSelect.setWhere(sqlSegment);
        }
    }

    /**
     * 数据范围权限sql处理
     * @param where 表达式
     * @return 新的表达式
     */
    protected Expression dataScope(Expression where) {
        DataPermScope dataPermScope = dataPermScopeHandler.getDataPermScope();
        Expression queryExpression;
        DataScopeEnum scopeType = dataPermScope.getScopeType();
        switch (scopeType){
            case SELF:{
                queryExpression = this.selfScope();
                break;
            }
            case DEPT_SCOPE:{
                Expression deptScopeExpression = this.deptScope(dataPermScope.getDeptScopeIds());
                // 追加查询自身的数据
                queryExpression = new OrExpression(deptScopeExpression,this.selfScope());
                break;
            }
            case USER_SCOPE:{
                queryExpression = this.userScope(dataPermScope.getUserScopeIds());
                break;
            }
            case DEPT_AND_USER_SCOPE:{
                queryExpression = this.deptAndUserScope(dataPermScope.getDeptScopeIds(), dataPermScope.getUserScopeIds());
                break;
            }
            case BELONG_DEPT:
            case BELONG_DEPT_AND_SUB: {
                queryExpression = this.deptScope(dataPermScope.getDeptScopeIds());
                break;
            }
            case ALL_SCOPE:
                return where;
            default:{
                throw new BusinessException("代码有问题");
            }
        }

        return new AndExpression(new Parenthesis(queryExpression), where);
    }

    /**
     * 查询自己的数据
     */
    protected Expression selfScope(){
        Long userId = DataPermContextHolder.getUserDetail()
			.map(SecurityUser::getUserId)
			.orElseThrow(() -> {
				throw new BusinessException("用户未登录");
			});
        return new EqualsTo(new Column(CREATOR),new LongValue(userId));
    }

    /**
     * 查询用户范围的数据
     */
    protected Expression userScope(Set<Long> userScopeIds){
        Long userId = DataPermContextHolder.getUserDetail()
			.map(SecurityUser::getUserId)
			.orElseThrow(() -> {
				throw new BusinessException("用户未登录");
			});
        List<Expression> userExpressions = Optional.ofNullable(userScopeIds).orElse(new HashSet<>()).stream()
                .map(LongValue::new)
                .collect(Collectors.toList());
        // 追加自身
        userExpressions.add(new LongValue(userId));
        return new InExpression(new Column(CREATOR), new ExpressionList(userExpressions));
    }

    /**
     * 查询部门范围的数据
     */
    protected Expression deptScope(Set<Long> deptIds){
        DataPermProperties.DataPerm dataPerm = dataPermProperties.getDataPerm();

        // 创建嵌套子查询
        PlainSelect plainSelect = new PlainSelect();
        // 设置查询字段
        SelectExpressionItem selectItem = new SelectExpressionItem();
        selectItem.setExpression(new Column(dataPerm.getQueryField()));
        plainSelect.addSelectItems(selectItem);
        // 过滤重复的子查询结果
        plainSelect.setDistinct(new Distinct());
        // 设置所查询表
        plainSelect.setFromItem(new Table(dataPerm.getTable()));

        // 构建查询条件
        List<Expression> deptExpressions = Optional.ofNullable(deptIds).orElse(new HashSet<>()).stream()
                .map(LongValue::new)
                .collect(Collectors.toList());
        // 构造空查询
        if (deptExpressions.size() == 0){
            deptExpressions.add(null);
        }
        // 设置查询条件
        plainSelect.setWhere(new InExpression(new Column(dataPerm.getWhereField()), new ExpressionList(deptExpressions)));

        // 拼接子查询
        SubSelect subSelect = new SubSelect();
        subSelect.setSelectBody(plainSelect);
        return new InExpression(new Column(CREATOR), subSelect);
    }

    /**
     * 查询部门和用户范围的数据
     */
    protected Expression deptAndUserScope(Set<Long> deptScopeIds, Set<Long> userScopeIds){
        Expression deptScopeExpression = this.deptScope(deptScopeIds);
        Expression userScopeExpression = this.userScope(userScopeIds);
        return new OrExpression(deptScopeExpression,userScopeExpression);
    }
}
