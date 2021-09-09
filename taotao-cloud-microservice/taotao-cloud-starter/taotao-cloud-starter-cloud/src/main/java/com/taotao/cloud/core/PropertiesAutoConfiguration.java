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
package com.taotao.cloud.core;

import com.taotao.cloud.core.properties.CoreThreadPoolProperties;
import com.taotao.cloud.core.properties.HttpClientProperties;
import com.taotao.cloud.core.properties.CoreProperties;
import com.taotao.cloud.core.properties.IpRegexProperties;
import com.taotao.cloud.core.properties.MonitorThreadPoolProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * PropertiesAutoConfiguration 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 21:05:48
 */
@EnableConfigurationProperties({
	CoreThreadPoolProperties.class,
	CoreProperties.class,
	HttpClientProperties.class,
	IpRegexProperties.class,
	MonitorThreadPoolProperties.class,
})
public class PropertiesAutoConfiguration {

}
