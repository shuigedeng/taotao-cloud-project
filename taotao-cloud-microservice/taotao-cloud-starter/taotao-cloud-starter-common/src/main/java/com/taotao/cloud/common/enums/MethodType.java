package com.taotao.cloud.common.enums;


/**
 * 方法类型
 *
 * @author shuigedeng
 */
public enum MethodType {

	/**
	 * 方法类型 GET PUT POST DELETE OPTIONS
	 */
	GET(false),
	PUT(true),
	POST(true),
	DELETE(false),
	HEAD(false),
	OPTIONS(false);

	private final boolean hasContent;

	MethodType(boolean hasContent) {
		this.hasContent = hasContent;
	}

	public boolean isHasContent() {
		return hasContent;
	}

	@Override
	public String toString() {
		return "MethodType{" +
			"hasContent=" + hasContent +
			"} " + super.toString();
	}
}
