package com.taotao.cloud.sys.biz.entity.config;

import java.io.Serializable;
import lombok.Data;

/**
 * 积分签到设置
 */
@Data
public class PointSettingItem implements Comparable<PointSettingItem>, Serializable {

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
	public int compareTo(PointSettingItem pointSettingItem) {
		return this.day - pointSettingItem.getDay();
	}
}
