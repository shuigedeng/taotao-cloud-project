package com.taotao.cloud.common.enums;

import java.util.Arrays;

public enum LoginTypeEnum {

	/**
	 * 用户+密码登录
	 */
	B_PC_ACCOUNT("b_pc_account", "b端用户-> 用户+密码登录"),
	C_PC_ACCOUNT("c_pc_account", "c端用户之pc端-> 用户+密码登录"),
	C_APP_ACCOUNT("c_app_account", "c端用户之APP端-> 用户+密码登录"),

	/**
	 * 用户+密码+验证码登录
	 */
	B_PC_ACCOUNT_VERIFICATION("b_pc_account_verification",
		"b端用户-> 用户+密码+验证码登录"),
	C_PC_ACCOUNT_VERIFICATION("c_pc_account_verification", "c端用户之pc端-> 用户+密码+验证码登录"),

	/**
	 * 手机短信登录
	 */
	B_PC_PHONE("b_pc_phone", "b端用户-> 手机号码+短信登录"),
	C_PC_PHONE("c_pc_phone", "c端用户之pc端-> 手机号码+短信登录"),
	C_MIMI_PHONE("c_mimi_phone", "c端用户之pc端-> 手机号码+短信登录"),
	C_APP_PHONE("c_app_phone", "c端用户之pc端-> 手机号码+短信登录"),

	/**
	 * 面部识别登录
	 */
	C_APP_FACE("c_app_face", "c端用户之app端-> 面部识别登录"),
	/**
	 * c端用户之pc端 -> 用户+密码登录 手机扫码登录 手机号码+短信登录 第三方登录(qq登录 微信登录 支付宝登录 github/gitee/weibo/抖音/钉钉/gitlab
	 * 等等)
	 */
	C_PC_QR_CODE("c_pc_qr_code", "c端用户之pc端-> 手机扫码登录"),
	C_PC_QQ("c_pc_qq", "c端用户之pc端-> qq登录"),
	c_pc_wechat("c_pc_wechat", "c端用户之pc端-> 微信登录"),
	C_PC_ALIPAY("c_pc_alipay", "c端用户之pc端-> 支付宝登录"),
	C_PC_GITHUB("c_pc_github", "c端用户之pc端-> github登录"),
	C_PC_GITEE("c_pc_gitee", "c端用户之pc端-> gitee登录"),
	C_PC_WEIBO("c_pc_weibo", "c端用户之pc端-> weibo登录"),
	C_PC_TIKTOK("c_pc_tiktok", "c端用户之pc端-> 抖音登录"),
	C_PC_DINGDING("c_pc_dingding", "c端用户之pc端-> 钉钉登录"),
	C_PC_GITLAB("c_pc_gitlab", "c端用户之pc端-> gitlab登录"),
	/**
	 * 微信一键登录
	 */
	C_MIMI_ONE_CLICK("c_mimi_one_click", "c端用户之小程序端-> 微信一键登录"),
	/**
	 * c端用户之微信公众号 -> 微信公众号登录
	 */
	C_MP_ONE_CLICK("c_mp_one_click", "c端用户之公众号端-> 微信公众号登录"),
	/**
	 * c端用户之app -> 短信密码登录 本机号码一键登录(不需要密码) 手机号码+短信登录 指纹登录 面部识别登录 手势登录 第三方登录(qq登录 微信登录 支付宝登录)
	 */
	C_APP_ONE_CLICK("c_app_one_click", "c端用户之app端-> 本机号码一键登录(不需要密码)"),
	C_APP_FINGERPRINT("c_app_fingerprint", "c端用户之app端-> 指纹登录"),
	C_APP_FINGER("c_app_finger", "c端用户之app端-> 手势登录"),
	C_APP_QQ("c_app_qq", "c端用户之app端-> qq登录"),
	C_APP_WECHAT("c_app_wechat", "c端用户之app端-> 微信登录"),
	C_APP_ALIPAY("c_app_alipay", "c端用户之app端-> 支付宝登录");

	/**
	 * 类型
	 */
	private final String type;

	/**
	 * 描述
	 */
	private final String description;

	public static Boolean hasType(String type) {
		return Arrays.stream(values()).anyMatch(x -> x.getType().equals(type));
	}

	LoginTypeEnum(String type, String description) {
		this.type = type;
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public String getDescription() {
		return description;
	}
}
