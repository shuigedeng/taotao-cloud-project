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

package com.taotao.cloud.ip2region.configuration;

import com.taotao.cloud.ip2region.impl.Ip2regionSearcherImpl;
import com.taotao.cloud.ip2region.model.Ip2regionSearcher;
import com.taotao.cloud.ip2region.properties.Ip2regionProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
//import org.springframework.nativex.hint.NativeHint;
//import org.springframework.nativex.hint.ResourceHint;

/**
 * ip2region 自动化配置
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@Configuration
@ConditionalOnProperty(prefix = Ip2regionProperties.PREFIX, name = "enabled", havingValue = "true")
//@NativeHint(resources = @ResourceHint(patterns = "^ip2region/ip2region.db"))
public class Ip2regionAutoConfiguration {

	@Bean
	public Ip2regionSearcher ip2regionSearcher(ResourceLoader resourceLoader,
		Ip2regionProperties properties) {
		return new Ip2regionSearcherImpl(resourceLoader, properties);
	}

}
