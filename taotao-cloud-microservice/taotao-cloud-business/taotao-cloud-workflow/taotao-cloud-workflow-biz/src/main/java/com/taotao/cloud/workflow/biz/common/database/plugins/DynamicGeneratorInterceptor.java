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

import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.dynamic.datasource.aop.DynamicDataSourceAnnotationInterceptor;
import com.baomidou.dynamic.datasource.creator.DefaultDataSourceCreator;
import com.baomidou.dynamic.datasource.processor.DsProcessor;
import com.baomidou.dynamic.datasource.spring.boot.autoconfigure.DataSourceProperty;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import org.aopalliance.intercept.MethodInvocation;

public class DynamicGeneratorInterceptor extends DynamicDataSourceAnnotationInterceptor {

    private DynamicRoutingDataSource dynamicRoutingDataSource;
    private DefaultDataSourceCreator dataSourceCreator;

    public DynamicGeneratorInterceptor(Boolean allowedPublicOnly, DsProcessor dsProcessor) {
        super(allowedPublicOnly, dsProcessor);
    }

    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        try {
            // 是否继承动态生成源接口
            if (methodInvocation.getThis() instanceof DynamicSourceGeneratorInterface) {
                DS ds = methodInvocation.getThis().getClass().getAnnotation(DS.class);
                if (ds != null && StringUtil.isNotEmpty(ds.value())) {
                    String datasourceName = ds.value();
                    DynamicSourceGeneratorInterface m = (DynamicSourceGeneratorInterface) methodInvocation.getThis();
                    String now = null;
                    try {
                        boolean invalid = true;
                        if (Boolean.TRUE.equals(m.cachedConnection())) {
                            if (dynamicRoutingDataSource == null) {
                                dynamicRoutingDataSource = SpringContext.getBean(DynamicRoutingDataSource.class);
                                dataSourceCreator = SpringContext.getBean(DefaultDataSourceCreator.class);
                            }
                            if (dynamicRoutingDataSource.getDataSources().containsKey(datasourceName)) {
                                //                                if
                                // (dynamicRoutingDataSource.getCurrentDataSources().get(datasourceName).getConnection().isValid(5)) {
                                // 已存在当前动态数据源且数据源可用则不重新获取数据源配置
                                invalid = false;
                                //                                }
                            }
                        }
                        if (invalid) {
                            // 重新生成动态数据源
                            // 设置为默认数据源获取动态数据源信息
                            now = DynamicDataSourceContextHolder.push(null);
                            DataSourceUtil dataSource = m.getDataSource();
                            if (dataSource != null) {
                                DataSourceProperty dataSourceProperty = new DataSourceProperty();
                                dataSourceProperty.setUsername(dataSource.getUserName());
                                dataSourceProperty.setPassword(dataSource.getPassword());
                                dataSourceProperty.setUrl(ConnUtil.getUrl(dataSource));
                                dataSourceProperty.setDriverClassName(dataSource.getDriver());
                                dynamicRoutingDataSource.addDataSource(
                                        datasourceName, dataSourceCreator.createDataSource(dataSourceProperty));
                            }
                        }
                    } finally {
                        if (now != null) {
                            DynamicDataSourceContextHolder.poll();
                        }
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return methodInvocation.proceed();
    }
}
