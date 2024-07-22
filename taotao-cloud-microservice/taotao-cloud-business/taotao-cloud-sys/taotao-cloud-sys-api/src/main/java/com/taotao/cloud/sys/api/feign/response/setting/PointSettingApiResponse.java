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

package com.taotao.cloud.sys.api.feign.response.setting;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 积分设置
 */
@Data
public class PointSettingApiResponse implements Serializable {

	@Serial
	private static final long serialVersionUID = -4261856614779031745L;
	/**
	 * 注册
	 */
	private Integer register;
	/**
	 * 消费1元赠送多少积分
	 */
	private Integer consumer;
	/**
	 * 积分付款X积分=1元
	 */
	private Integer money;
	/**
	 * 每日签到积分
	 */
	private Integer signIn;
	/**
	 * 订单评价赠送积分
	 */
	private Integer comment;
	/**
	 * 积分具体设置
	 */
	private List<PointSettingItemVO> pointSettingItems;

	public Integer getRegister() {
		if (register == null || register < 0) {
			return 0;
		}
		return register;
	}

	public Integer getMoney() {
		if (money == null || money < 0) {
			return 0;
		}
		return money;
	}

	public Integer getConsumer() {
		if (consumer == null || consumer < 0) {
			return 0;
		}
		return consumer;
	}

	public Integer getSignIn() {
		if (signIn == null || signIn < 0) {
			return 0;
		}
		return signIn;
	}

	public Integer getComment() {
		if (comment == null || comment < 0) {
			return 0;
		}
		return comment;
	}


	/**
	 * 积分签到设置
	 */
	@Data
	public static class PointSettingItemVO implements
		Comparable<PointSettingItemVO>,
		Serializable {

		/**
		 * 签到天数
		 */
		private Integer day;

		/**
		 * 赠送积分
		 */
		private Integer point;

		public Integer getPoint() {
			if (point == null || point < 0) {
				return 0;
			}
			return point;
		}

		public void setPoint(Integer point) {
			this.point = point;
		}

		@Override
		public int compareTo(
			PointSettingItemVO pointSettingItem) {
			// return this.day - pointSettingItem.getDay();
			return 0;
		}
	}

}
