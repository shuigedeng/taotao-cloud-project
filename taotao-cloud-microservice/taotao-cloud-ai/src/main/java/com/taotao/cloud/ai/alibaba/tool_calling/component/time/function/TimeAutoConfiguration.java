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

package com.taotao.cloud.ai.alibaba.tool_calling.component.time.function;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
@ConditionalOnClass({GetCurrentTimeByTimeZoneIdService.class})
@ConditionalOnProperty(
        prefix = "spring.ai.toolcalling.time",
        name = "enabled",
        havingValue = "true")
public class TimeAutoConfiguration {

    @Bean(name = "getCityTimeFunction")
    @ConditionalOnMissingBean
    @Description("Get the time of a specified city.")
    public GetCurrentTimeByTimeZoneIdService getCityTimeFunction() {
        return new GetCurrentTimeByTimeZoneIdService();
    }
}
