/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.data.mybatis.plus.datascope;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.db.Db;
import cn.hutool.db.Entity;
import com.baomidou.mybatisplus.core.toolkit.PluginUtils;
import com.baomidou.mybatisplus.extension.handlers.AbstractSqlParserHandler;
import com.taotao.cloud.common.exception.CheckedException;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.common.utils.SecurityUtil;
import com.taotao.cloud.data.mybatis.plus.enums.DataScopeTypeEnum;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import javax.sql.DataSource;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.springframework.security.core.GrantedAuthority;

/**
 * Mybatis 拦截器 主要用于数据权限拦截
 *
 * @author shuigedeng
 * @since 2020/5/2 16:40
 * @version 1.0.0
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataScopeInterceptor extends AbstractSqlParserHandler implements Interceptor {

    private final DataSource dataSource;

	public DataScopeInterceptor(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (LogUtil.isInfoEnabled()) {
	        LogUtil.debug("进入 PrepareInterceptor 拦截器...");
        }

        StatementHandler statementHandler = PluginUtils.realTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(statementHandler);
        this.sqlParser(metaObject);
        // 先判断是不是SELECT操作 不是直接过滤
        MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        if (!SqlCommandType.SELECT.equals(mappedStatement.getSqlCommandType())) {
            return invocation.proceed();
        }
        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        // 执行的SQL语句
        String originalSql = boundSql.getSql();
        // SQL语句的参数
        Object parameterObject = boundSql.getParameterObject();

        //查找参数中包含DataScope类型的参数
        DataScope dataScope = findDataScopeObject(parameterObject);
        if (ObjectUtil.isNull(dataScope)) {
            return invocation.proceed();
        }

        String scopeFiledName = dataScope.getScopeFiledName();
        List<Integer> deptIds = dataScope.getDeptIds();
        // 优先获取赋值数据
        if (CollUtil.isEmpty(deptIds)) {
            SecurityUser user = SecurityUtil.getUser();
            if (user == null) {
                throw new CheckedException("auto datascope, set up security details true");
            }

            // 解析角色Id
            List<String> roleIdList = user.getAuthorities()
                    .stream().map(GrantedAuthority::getAuthority)
                    .filter(authority -> authority.startsWith("ROLE_"))
                    .map(authority -> authority.split("_")[1])
                    .collect(Collectors.toList());

            // 通过角色Id查询范围权限
            Entity query = Db.use(dataSource)
                    .query("SELECT * FROM sys_role where role_id IN (" + CollUtil.join(roleIdList, ",") + ")")
                    .stream().min(Comparator.comparingInt(o -> o.getInt("ds_type"))).get();

            // 数据库权限范围字段
            Integer dsType = query.getInt("ds_type");
            // 查询全部
            if (DataScopeTypeEnum.ALL.getType() == dsType) {
                return invocation.proceed();
            }

            // 除了全部 则要获取自定义 本级及其下级 查询本级
            String dsScope = query.getStr("ds_scope");

            deptIds.addAll(Arrays.stream(dsScope.split(","))
                    .map(Integer::parseInt).collect(Collectors.toList()));

            String join = CollectionUtil.join(deptIds, ",");
            originalSql = "select * from (" + originalSql + ") temp_data_scope where temp_data_scope." + scopeFiledName + " in (" + join + ")";
            metaObject.setValue("delegate.boundSql.sql", originalSql);

            return invocation.proceed();
        }
        return invocation.proceed();
    }

    /**
     * 生成拦截对象的代理
     *
     * @param target 目标对象
     * @return 代理对象
     */
    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        }
        return target;
    }

    /**
     * mybatis配置的属性
     *
     * @param properties mybatis配置的属性
     */
    @Override
    public void setProperties(Properties properties) {

    }

    /**
     * 查找参数是否包括DataScope对象
     *
     * @param parameterObj 参数列表
     * @return DataScope
     */
    private DataScope findDataScopeObject(Object parameterObj) {
        if (parameterObj instanceof DataScope) {
            return (DataScope) parameterObj;
        } else if (parameterObj instanceof Map) {
            for (Object val : ((Map<?, ?>) parameterObj).values()) {
                if (val instanceof DataScope) {
                    return (DataScope) val;
                }
            }
        }
        return null;
    }
}
