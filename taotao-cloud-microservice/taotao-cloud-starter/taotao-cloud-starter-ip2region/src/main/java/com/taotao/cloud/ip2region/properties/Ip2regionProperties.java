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

package com.taotao.cloud.ip2region.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * ip2region 配置类
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:01:42
 */
@RefreshScope
@ConfigurationProperties(Ip2regionProperties.PREFIX)
public class Ip2regionProperties {

	public static final String PREFIX = "taotao.cloud.ip2region";

	/**
	 * ip2region.db 文件路径
	 */
	private String dbFileLocation = "classpath:ip2region/ip2region.db";

	public String getDbFileLocation() {
		return dbFileLocation;
	}

	public void setDbFileLocation(String dbFileLocation) {
		this.dbFileLocation = dbFileLocation;
	}
}
