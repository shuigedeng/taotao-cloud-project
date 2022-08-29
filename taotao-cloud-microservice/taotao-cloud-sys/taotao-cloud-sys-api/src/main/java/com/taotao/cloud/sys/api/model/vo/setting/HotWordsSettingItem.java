package com.taotao.cloud.sys.api.model.vo.setting;

import lombok.Data;

import java.io.Serializable;

/**
 * 积分签到设置
 */
@Data
public class HotWordsSettingItem implements Comparable<HotWordsSettingItem>, Serializable {


	/**
	 * 热词
	 */
	private String keywords;


	/**
	 * 默认分数
	 */
	private Integer score;


	public Integer getScore() {
		if (score == null || score < 0) {
			return 0;
		}
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Override
	public int compareTo(HotWordsSettingItem pointSettingItem) {
		return pointSettingItem.getScore();
	}
}
