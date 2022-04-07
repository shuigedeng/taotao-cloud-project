package com.taotao.cloud.common.enums;

/**
 * token角色类型
 */
public enum UserEnum implements BaseEnum {
	/**
	 * 会员
	 */
	MEMBER(1, "会员"),
	/**
	 * 商家
	 */
	STORE(2, "商家"),
	/**
	 * 管理员
	 */
	MANAGER(3, "管理员"),
	/**
	 * 系统
	 */
	SYSTEM(4, "系统");

	private final int code;
	private final String desc;

	UserEnum(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public String getNameByCode(int code) {
		for (UserEnum result : UserEnum.values()) {
			if (result.getCode() == code) {
				return result.name().toLowerCase();
			}
		}
		return null;
	}

	@Override
	public int getCode() {
		return code;
	}

	public static String getByCode(int code) {
		for (UserEnum result : UserEnum.values()) {
			if (result.getCode() == code) {
				return result.name().toLowerCase();
			}
		}
		return null;
	}

	public static UserEnum getEnumByCode(int code) {
		for (UserEnum result : UserEnum.values()) {
			if (result.getCode() == code) {
				return result;
			}
		}
		return null;
	}

}
