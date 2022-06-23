package com.taotao.cloud.sys.api.web.vo.setting;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 积分设置
 */
@Data
public class PointSettingVO implements Serializable {

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
}
