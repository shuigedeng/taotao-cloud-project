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

package com.taotao.cloud.laytpl.configuration;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.laytpl.model.FmtFunc;
import com.taotao.cloud.laytpl.model.LayTplTemplate;
import com.taotao.cloud.laytpl.properties.LayTplProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * laytpl 自动化配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@Configuration
@EnableConfigurationProperties({LayTplProperties.class})
@ConditionalOnProperty(prefix = LayTplProperties.PREFIX, name = "enabled", havingValue = "true")
public class LayTplAutoConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(LayTplAutoConfiguration.class, StarterName.LAYTPL_STARTER);
	}

	@Bean("fmt")
	public FmtFunc fmtFunc(LayTplProperties properties) {
		return new FmtFunc(properties);
	}

	@Bean("layTpl")
	public LayTplTemplate layTplTemplate(FmtFunc fmtFunc, LayTplProperties properties) {
		return new LayTplTemplate(properties, fmtFunc);
	}
}
