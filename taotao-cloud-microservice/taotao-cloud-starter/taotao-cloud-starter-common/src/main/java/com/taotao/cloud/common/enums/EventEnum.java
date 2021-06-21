package com.taotao.cloud.common.enums;

import java.util.HashMap;

/**
 * @author: chejiangyi
 * @version: 2019-08-10 16:00
 **/
public enum EventEnum {
	/**
	 * 属性缓存更新事件
	 */
	PropertyCacheUpdateEvent(new HashMap<String, Object>().getClass(), "属性缓存更新事件");

	/**
	 * 类
	 */
	Class dataClass;

	/**
	 * 描述
	 */
	String desc;


	public Class getDataClass() {
		return dataClass;
	}

	EventEnum(Class dataClass, String desc) {
		this.desc = desc;
		this.dataClass = dataClass;
	}
}
