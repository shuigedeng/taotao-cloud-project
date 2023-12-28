/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.workflow.biz.common.database.plugins;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.support.DdConstants;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

/**
 * Master-slave Separation Plugin with mybatis
 *
 */
@Intercepts({
    @Signature(
            type = Executor.class,
            method = "query",
            args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(
            type = Executor.class,
            method = "query",
            args = {
                MappedStatement.class,
                Object.class,
                RowBounds.class,
                ResultHandler.class,
                CacheKey.class,
                BoundSql.class
            }),
    @Signature(
            type = Executor.class,
            method = "update",
            args = {MappedStatement.class, Object.class})
})
@Slf4j
public class MyMasterSlaveAutoRoutingPlugin implements Interceptor {

    protected DynamicRoutingDataSource dynamicDataSource;

    public MyMasterSlaveAutoRoutingPlugin(DataSource dataSource) {
        this.dynamicDataSource = (DynamicRoutingDataSource) dataSource;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] args = invocation.getArgs();
        if (!"true".equals(DataSourceContextHolder.getDatasourceName())) {
            return invocation.proceed();
        }
        MappedStatement ms = (MappedStatement) args[0];
        String pushedDataSource = null;
        try {
            String tenantId = StringUtil.isNotEmpty(DataSourceContextHolder.getDatasourceId())
                    ? DataSourceContextHolder.getDatasourceId()
                    : "";
            // 判断切库
            String dataSource =
                    SqlCommandType.SELECT == ms.getSqlCommandType() ? DdConstants.SLAVE : DdConstants.MASTER;
            // 如果是从库
            if (DdConstants.SLAVE.equals(dataSource)) {
                // 判断从库不存在
                if (!dynamicDataSource.getGroupDataSources().containsKey(tenantId + "-" + DdConstants.SLAVE)) {
                    // 判断主库存在（有主库没从库）
                    if (dynamicDataSource.getGroupDataSources().containsKey(tenantId + "-" + DdConstants.MASTER)) {
                        dataSource = tenantId + "-" + DdConstants.MASTER;
                    }
                } else {
                    dataSource = tenantId + "-" + DdConstants.SLAVE;
                }
            } else {
                // 如果是主库
                dataSource = tenantId + "-" + dataSource;
            }
            if (!dynamicDataSource.getGroupDataSources().containsKey(dataSource)) {
                dataSource = tenantId + "-" + dataSource;
                RedisUtil redisUtil = SpringContext.getBean(RedisUtil.class);
                if (redisUtil.exists(tenantId)) {
                    Object string = redisUtil.getString(tenantId);
                    List<TenantLinkModel> linkList =
                            JsonUtil.getJsonToList(String.valueOf(string), TenantLinkModel.class);
                    // 添加数据源信息到redis中
                    List<String> list = new ArrayList<>(16);
                    for (TenantLinkModel model : linkList) {
                        DruidDataSource druidDataSource = new DruidDataSource();
                        // 使用自定义URL
                        if (StringUtil.isNotEmpty(model.getConnectionStr())) {
                            druidDataSource.setUrl(model.getConnectionStr());
                        } else {
                            // url获取
                            druidDataSource.setUrl(ParameterUtil.getUrl(model));
                        }
                        druidDataSource.setUsername(model.getUserName());
                        druidDataSource.setPassword(model.getPassword());
                        try {
                            druidDataSource.setDriverClassName(
                                    DbTypeUtil.getDriver(model.getDbType()).getDriver());
                        } catch (DataException e) {
                            LogUtils.error(e);
                        }

                        // 只需要将主库存起来
                        String masterCode = tenantId + "-master_" + RandomUtil.uuId();
                        String slaveCode = tenantId + "-slave_" + RandomUtil.uuId();
                        if ("0".equals(String.valueOf(model.getConfigType()))) {
                            dynamicDataSource.addDataSource(masterCode, druidDataSource);
                            list.add(masterCode);
                        } else {
                            dynamicDataSource.addDataSource(slaveCode, druidDataSource);
                            list.add(slaveCode);
                        }
                    }
                }
            }
            pushedDataSource = DynamicDataSourceContextHolder.push(dataSource);
            return invocation.proceed();
        } finally {
            if (pushedDataSource != null) {
                DynamicDataSourceContextHolder.poll();
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {}
}
