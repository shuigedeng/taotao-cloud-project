package com.taotao.cloud.sa.just.biz.just;


import me.zhyd.oauth.config.AuthSource;
import me.zhyd.oauth.request.AuthDefaultRequest;

/**
 * <p>
 * 扩展的自定义 source
 * </p>
 */
public enum ExtendSource implements AuthSource {

	/**
	 * 测试
	 */
	TEST {
		/**
		 * 授权的api
		 *
		 * @return url
		 */
		@Override
		public String authorize() {
			return "http://authorize";
		}

		/**
		 * 获取accessToken的api
		 *
		 * @return url
		 */
		@Override
		public String accessToken() {
			return "http://accessToken";
		}

		/**
		 * 获取用户信息的api
		 *
		 * @return url
		 */
		@Override
		public String userInfo() {
			return null;
		}

		/**
		 * 取消授权的api
		 *
		 * @return url
		 */
		@Override
		public String revoke() {
			return null;
		}

		/**
		 * 刷新授权的api
		 *
		 * @return url
		 */
		@Override
		public String refresh() {
			return null;
		}

		@Override
		public String getName() {
			return super.getName();
		}

		@Override
		public Class<? extends AuthDefaultRequest> getTargetClass() {
			return null;
		}
	}
}
