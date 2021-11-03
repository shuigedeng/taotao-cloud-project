/*
 * Copyright (c) 2019-2029, Dreamlu 卢春梦 (596392912@qq.com & www.dreamlu.net).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.laytpl.configuration;

import com.taotao.cloud.laytpl.model.FmtFunc;
import com.taotao.cloud.laytpl.model.LayTplTemplate;
import com.taotao.cloud.laytpl.properties.LayTplProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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
@ConditionalOnProperty(prefix = LayTplProperties.PREFIX, name = "enabled", havingValue = "true")
public class LayTplAutoConfiguration {

	@Bean("fmt")
	public FmtFunc fmtFunc(LayTplProperties properties) {
		return new FmtFunc(properties);
	}

	@Bean("layTpl")
	public LayTplTemplate micaTemplate(FmtFunc fmtFunc, LayTplProperties properties) {
		return new LayTplTemplate(properties, fmtFunc);
	}
}
