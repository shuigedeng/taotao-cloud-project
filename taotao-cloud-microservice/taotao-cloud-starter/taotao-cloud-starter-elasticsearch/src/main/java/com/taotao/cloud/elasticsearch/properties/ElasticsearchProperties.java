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
package com.taotao.cloud.elasticsearch.properties;

import com.taotao.cloud.elasticsearch.constant.ElasticsearchConstant;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 索引配置
 *
 * @author dengtao
 * @version 1.0.0
 * @since 2020/5/3 08:00
 */
@Data
@RefreshScope
@ConfigurationProperties(prefix = ElasticsearchConstant.BASE_ELASTICSEARCH_PREFIX)
public class ElasticsearchProperties {

	/**
	 * Elasticsearch 总开关
	 */
	private boolean enabled = false;
}
