/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Engine 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/dante-engine
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.oauth2.core.properties;

import com.google.common.base.MoreObjects;
import com.taotao.cloud.auth.biz.oauth2.core.constants.OAuth2Constants;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>Description: OAuth2 合规性配置参数 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/7/7 0:16
 */
@ConfigurationProperties(prefix = OAuth2Constants.PROPERTY_OAUTH2_COMPLIANCE)
public class OAuth2ComplianceProperties {

	private SignInEndpointLimited signInEndpointLimited = new SignInEndpointLimited();

	private SignInFailureLimited signInFailureLimited = new SignInFailureLimited();

	private SignInKickOutLimited signInKickOutLimited = new SignInKickOutLimited();

	public SignInEndpointLimited getSignInEndpointLimited() {
		return signInEndpointLimited;
	}

	public void setSignInEndpointLimited(SignInEndpointLimited signInEndpointLimited) {
		this.signInEndpointLimited = signInEndpointLimited;
	}

	public SignInFailureLimited getSignInFailureLimited() {
		return signInFailureLimited;
	}

	public void setSignInFailureLimited(SignInFailureLimited signInFailureLimited) {
		this.signInFailureLimited = signInFailureLimited;
	}

	public SignInKickOutLimited getSignInKickOutLimited() {
		return signInKickOutLimited;
	}

	public void setSignInKickOutLimited(SignInKickOutLimited signInKickOutLimited) {
		this.signInKickOutLimited = signInKickOutLimited;
	}

	public static class SignInFailureLimited {

		/**
		 * 是否开启登录失败检测，默认开启
		 */
		private Boolean enabled = true;

		/**
		 * 允许允许最大失败次数
		 */
		private Integer maxTimes = 5;

		/**
		 * 是否自动解锁被锁定用户，默认开启
		 */
		private Boolean autoUnlock = true;

		/**
		 * 记录失败次数的缓存过期时间，默认：2小时。
		 */
		private Duration expire = Duration.ofHours(2);

		public Boolean getEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

		public Integer getMaxTimes() {
			return maxTimes;
		}

		public void setMaxTimes(Integer maxTimes) {
			this.maxTimes = maxTimes;
		}

		public Duration getExpire() {
			return expire;
		}

		public void setExpire(Duration expire) {
			this.expire = expire;
		}

		public Boolean getAutoUnlock() {
			return autoUnlock;
		}

		public void setAutoUnlock(Boolean autoUnlock) {
			this.autoUnlock = autoUnlock;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this)
				.add("enabled", enabled)
				.add("maxTimes", maxTimes)
				.add("autoUnlock", autoUnlock)
				.add("expire", expire)
				.toString();
		}
	}

	public static class SignInEndpointLimited {

		/**
		 * 同一终端登录限制是否开启，默认开启。
		 */
		private Boolean enabled = false;

		/**
		 * 统一终端，允许同时登录的最大数量
		 */
		private Integer maximum = 1;

		public Boolean getEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

		public Integer getMaximum() {
			return maximum;
		}

		public void setMaximum(Integer maximum) {
			this.maximum = maximum;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this)
				.add("enabled", enabled)
				.add("maximum", maximum)
				.toString();
		}
	}

	public static class SignInKickOutLimited {

		/**
		 * 是否开启 Session 踢出功能，默认开启
		 */
		private Boolean enabled = true;

		public Boolean getEnabled() {
			return enabled;
		}

		public void setEnabled(Boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public String toString() {
			return MoreObjects.toStringHelper(this)
				.add("enabled", enabled)
				.toString();
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("signInEndpointLimited", signInEndpointLimited)
			.add("signInFailureLimited", signInFailureLimited)
			.add("signInKickOutLimited", signInKickOutLimited)
			.toString();
	}
}
