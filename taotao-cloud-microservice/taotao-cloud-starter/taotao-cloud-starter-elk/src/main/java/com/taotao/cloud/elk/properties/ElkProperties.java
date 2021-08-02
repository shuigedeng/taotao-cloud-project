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
package com.taotao.cloud.elk.properties;

import com.taotao.cloud.elk.constant.ElkConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * ElkProperties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/3 16:18
 */
@RefreshScope
@ConfigurationProperties(prefix = ElkConstant.BASE_ELK_PREFIX)
public class ElkProperties {

	private boolean enabled = false;

	private String appName = "";

	private String springAppName = "";

	private String[] destinations = {"127.0.0.1:4560"};
}
