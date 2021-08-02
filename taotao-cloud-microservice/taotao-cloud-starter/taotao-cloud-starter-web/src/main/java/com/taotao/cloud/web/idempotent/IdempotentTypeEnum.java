package com.taotao.cloud.web.idempotent;


/**
 * 幂等枚举类
 *
 * @author shuigedeng
 */
public enum IdempotentTypeEnum {

	/**
	 * 0+1
	 */
	ALL(0, "ALL"),
	/**
	 * ruid 是针对每一次请求的
	 */
	RID(1, "RID"),
	/**
	 * key+val 是针对相同参数请求
	 */
	KEY(2, "KEY");

	private final Integer index;
	private final String title;

	IdempotentTypeEnum(Integer index, String title) {
		this.index = index;
		this.title = title;
	}

	public Integer getIndex() {
		return index;
	}

	public String getTitle() {
		return title;
	}
}
