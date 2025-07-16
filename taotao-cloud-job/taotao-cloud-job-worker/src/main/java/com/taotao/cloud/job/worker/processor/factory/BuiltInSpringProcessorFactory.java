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

import com.taotao.cloud.job.worker.processor.ProcessorBean;
import com.taotao.cloud.job.worker.processor.ProcessorDefinition;
import com.taotao.cloud.job.worker.processor.type.BasicProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;

/**
 * 内建的 SpringBean 处理器工厂，用于加载 Spring 相关的Bean，非核心依赖
 *
 * @author shuigedeng
 * @since 2023/1/17
 */
@Slf4j
public class BuiltInSpringProcessorFactory extends AbstractBuildInSpringProcessorFactory {

    public BuiltInSpringProcessorFactory(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public ProcessorBean build(ProcessorDefinition processorDefinition) {

        try {
            boolean canLoad = checkCanLoad();
            if (!canLoad) {
                log.info(
                        "[ProcessorFactory] can't find Spring env, this processor can't load by 'BuiltInSpringProcessorFactory'");
                return null;
            }
            String processorInfo = processorDefinition.getProcessorInfo();
            // 用于区分方法级别的参数
            if (processorInfo.contains("#")) {
                return null;
            }
            BasicProcessor basicProcessor = getBean(processorInfo, applicationContext);
            return new ProcessorBean()
                    .setProcessor(basicProcessor)
                    .setClassLoader(basicProcessor.getClass().getClassLoader());
        } catch (NoSuchBeanDefinitionException ignore) {
            log.warn("[ProcessorFactory] can't find the processor in SPRING");
        } catch (Throwable t) {
            log.warn(
                    "[ProcessorFactory] load by BuiltInSpringProcessorFactory failed. If you are using Spring, make sure this bean was managed by Spring",
                    t);
        }

        return null;
    }
}
