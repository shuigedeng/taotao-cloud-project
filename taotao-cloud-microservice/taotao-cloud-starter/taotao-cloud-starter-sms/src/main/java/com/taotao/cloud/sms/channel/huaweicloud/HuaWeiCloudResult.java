/*
 * Copyright (c) 2018-2022 the original author or authors.
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.gnu.org/licenses/lgpl-3.0.html
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.channel.huaweicloud;

import java.util.Collection;

/**
 * 响应结果
 *
 * @author shuigedeng
 */
public class HuaWeiCloudResult {

	/**
	 * 成功代码
	 */
	public static final String SUCCESS_CODE = "000000";

	/**
	 * 请求返回的结果码。
	 */
	private String code;

	/**
	 * 请求返回的结果码描述。
	 */
	private String description;

	/**
	 * 短信ID列表，当目的号码存在多个时，每个号码都会返回一个SmsID。
	 * <p>
	 * 当返回异常响应时不携带此字段。
	 */
	private Collection<SmsID> result;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Collection<SmsID> getResult() {
		return result;
	}

	public void setResult(
		Collection<SmsID> result) {
		this.result = result;
	}

	/**
	 * 短信ID
	 */
	public static class SmsID {

		/**
		 * 短信的唯一标识。
		 */
		private String smsMsgId;

		/**
		 * 短信发送方的号码。
		 */
		private String from;

		/**
		 * 短信接收方的号码。
		 */
		private String originTo;

		/**
		 * 短信状态码
		 */
		private String status;

		/**
		 * 短信资源的创建时间，即短信平台接收到客户发送短信请求的时间，为UTC时间。
		 * <p>
		 * 格式为：yyyy-MM-dd'T'HH:mm:ss'Z'。
		 */
		private String createTime;

		public String getSmsMsgId() {
			return smsMsgId;
		}

		public void setSmsMsgId(String smsMsgId) {
			this.smsMsgId = smsMsgId;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getOriginTo() {
			return originTo;
		}

		public void setOriginTo(String originTo) {
			this.originTo = originTo;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getCreateTime() {
			return createTime;
		}

		public void setCreateTime(String createTime) {
			this.createTime = createTime;
		}
	}
}
