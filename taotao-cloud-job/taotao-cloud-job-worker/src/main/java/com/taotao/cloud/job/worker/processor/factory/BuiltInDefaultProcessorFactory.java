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
import com.taotao.cloud.job.worker.processor.ProcessorBean;
import com.taotao.cloud.job.worker.processor.ProcessorDefinition;
import com.taotao.cloud.job.worker.processor.type.BasicProcessor;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * 内建的默认处理器工厂，通过全限定类名加载处理器，但无法享受 IOC 框架的 DI 功能
 *
 * @author shuigedeng
 * @since 2023/1/17
 */
@Slf4j
public class BuiltInDefaultProcessorFactory implements ProcessorFactory {

    @Override
    public Set<String> supportTypes() {
        return Sets.newHashSet(ProcessorType.BUILT_IN.name());
    }

    @Override
    public ProcessorBean build(ProcessorDefinition processorDefinition) {

        String className = processorDefinition.getProcessorInfo();

        try {
            Class<?> clz = Class.forName(className);
            BasicProcessor basicProcessor =
                    (BasicProcessor) clz.getDeclaredConstructor().newInstance();
            return new ProcessorBean()
                    .setProcessor(basicProcessor)
                    .setClassLoader(basicProcessor.getClass().getClassLoader());
        } catch (Exception e) {
            log.warn(
                    "[ProcessorFactory] load local Processor(className = {}) failed.",
                    className,
                    e);
        }
        return null;
    }
}
