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

package com.taotao.cloud.third.client.configuration;

import com.github.lianjiatech.retrofit.spring.boot.core.RetrofitScan;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.support.factory.YamlPropertySourceFactory;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.third.client.properties.RetrofitProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;

/**
 * Retrofit 自动化配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@AutoConfiguration
@EnableConfigurationProperties({RetrofitProperties.class})
@ConditionalOnProperty(prefix = RetrofitProperties.PREFIX, name = "enabled", havingValue = "true")
@RetrofitScan(basePackages = "com.taotao.cloud.third.client.retrofit")
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:retrofit.yml")
public class RetrofitAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(RetrofitAutoConfiguration.class, StarterName.LAYTPL_STARTER);
	}
}
