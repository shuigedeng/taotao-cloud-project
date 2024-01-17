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

package com.taotao.cloud.workflow.biz.flowable.bpm.framework.bpm.config;

import com.taotao.cloud.flowable.biz.bpm.framework.bpm.core.event.BpmProcessInstanceResultEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** BPM 通用的 Configuration 配置类，提供给 Activiti 和 Flowable */
@Configuration(proxyBeanMethods = false)
public class BpmCommonConfiguration {

    @Bean
    public BpmProcessInstanceResultEventPublisher processInstanceResultEventPublisher(
            ApplicationEventPublisher publisher) {
        return new BpmProcessInstanceResultEventPublisher(publisher);
    }
}
