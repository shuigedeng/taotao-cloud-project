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

package com.taotao.cloud.workflow.biz.common.util;

import com.taotao.cloud.workflow.api.common.database.data.DataSourceContextHolder;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/** 提供一个全局的Spring线程池对象 */
@Configuration
@EnableAsync(proxyTargetClass = true)
@AllArgsConstructor
public class AsyncConfig implements AsyncConfigurer {

    @Primary
    @PostConstruct
    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 设置线程池核心容量
        executor.setCorePoolSize(5);
        // 设置线程池最大容量
        executor.setMaxPoolSize(50);
        // 设置任务队列长度
        executor.setQueueCapacity(10000);
        // 设置线程超时时间
        executor.setKeepAliveSeconds(120);
        // 设置线程名称前缀
        executor.setThreadNamePrefix("sysTaskExecutor");
        // 设置任务丢弃后的处理策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setTaskDecorator(r -> {
            // 实现线程上下文穿透， 异步线程内无法获取之前的Request，租户信息等， 如有新的上下文对象在此处添加
            RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
            String dataSourceId = DataSourceContextHolder.getDatasourceId();
            String dataSourceName = DataSourceContextHolder.getDatasourceName();
            return () -> {
                try {
                    if (attributes != null) {
                        RequestContextHolder.setRequestAttributes(attributes);
                    }
                    if (dataSourceId != null || dataSourceName != null) {
                        DataSourceContextHolder.setDatasource(dataSourceId, dataSourceName);
                    }
                    r.run();
                } finally {
                    RequestContextHolder.resetRequestAttributes();
                    DataSourceContextHolder.clearDatasourceType();
                }
            };
        });
        return executor;
    }
}
