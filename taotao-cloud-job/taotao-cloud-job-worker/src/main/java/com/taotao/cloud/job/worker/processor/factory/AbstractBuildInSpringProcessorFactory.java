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

package com.taotao.cloud.job.worker.processor.factory;

import com.google.common.collect.Sets;
import com.taotao.cloud.job.common.enums.ProcessorType;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
public abstract class AbstractBuildInSpringProcessorFactory implements ProcessorFactory {

    protected final ApplicationContext applicationContext;

    protected AbstractBuildInSpringProcessorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Set<String> supportTypes() {
        return Sets.newHashSet(ProcessorType.BUILT_IN.name());
    }

    protected boolean checkCanLoad() {
        try {
            ApplicationContext.class.getClassLoader();
            return applicationContext != null;
        } catch (Throwable ignore) {
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    protected static <T> T getBean(String className, ApplicationContext ctx) throws Exception {

        // 0. 尝试直接用 Bean 名称加载
        try {
            final Object bean = ctx.getBean(className);
            if (bean != null) {
                return (T) bean;
            }
        } catch (Exception ignore) {
        }

        // 1. ClassLoader 存在，则直接使用 clz 加载
        ClassLoader classLoader = ctx.getClassLoader();
        if (classLoader != null) {
            return (T) ctx.getBean(classLoader.loadClass(className));
        }
        // 2. ClassLoader 不存在(系统类加载器不可见)，尝试用类名称小写加载
        String[] split = className.split("\\.");
        String beanName = split[split.length - 1];
        // 小写转大写
        char[] cs = beanName.toCharArray();
        cs[0] += 32;
        String beanName0 = String.valueOf(cs);
        log.warn(
                "[SpringUtils] can't get ClassLoader from context[{}], try to load by beanName:{}",
                ctx,
                beanName0);
        return (T) ctx.getBean(beanName0);
    }
}
