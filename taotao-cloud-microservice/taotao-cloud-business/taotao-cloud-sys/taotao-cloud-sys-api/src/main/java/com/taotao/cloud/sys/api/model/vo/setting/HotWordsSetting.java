package com.taotao.cloud.sys.api.model.vo.setting;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 搜索热词
 */
@Data
public class HotWordsSetting implements Serializable {

	//热词1-5，默认分数1-5

	/**
	 * 热词默认配置
	 */
	private List<HotWordsSettingItem> hotWordsSettingItems = new ArrayList<>();

	/**
	 * 每日保存数量
	 */
	private Integer saveNum;

}
