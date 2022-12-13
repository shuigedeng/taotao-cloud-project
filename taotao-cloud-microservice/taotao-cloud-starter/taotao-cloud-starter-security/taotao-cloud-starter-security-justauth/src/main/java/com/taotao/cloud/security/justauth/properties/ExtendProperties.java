/*
 * Copyright (c) 2019-2029, xkcoding & Yangkai.Shen & 沈扬凯 (237497819@qq.com & xkcoding.com).
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
 *
 */

package com.taotao.cloud.security.justauth.properties;

import java.util.HashMap;
import java.util.Map;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.request.AuthRequest;

/**
 * <p>
 * 扩展配置
 * </p>
 */
public class ExtendProperties {

	/**
	 * 枚举类全路径
	 */
	private Class<? extends AuthSource> enumClass;

	private Map<String, ExtendRequestConfig> config = new HashMap<>();


	public Class<? extends AuthSource> getEnumClass() {
		return enumClass;
	}

	public void setEnumClass(Class<? extends AuthSource> enumClass) {
		this.enumClass = enumClass;
	}

	public Map<String, ExtendRequestConfig> getConfig() {
		return config;
	}

	public void setConfig(
		Map<String, ExtendRequestConfig> config) {
		this.config = config;
	}

	public static class ExtendRequestConfig extends AuthConfig {

		/**
		 * 请求对应全路径
		 */
		private Class<? extends AuthRequest> requestClass;


		public Class<? extends AuthRequest> getRequestClass() {
			return requestClass;
		}

		public void setRequestClass(Class<? extends AuthRequest> requestClass) {
			this.requestClass = requestClass;
		}
	}

}
