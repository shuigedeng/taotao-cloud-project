package com.taotao.cloud.member.biz.connect.entity.dto;

import com.alibaba.fastjson.JSONObject;
import com.taotao.cloud.member.biz.connect.config.ConnectAuthEnum;
import com.taotao.cloud.member.biz.connect.entity.enums.AuthUserGender;

import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ConnectAuthUser
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConnectAuthUser implements Serializable {

	@Serial
	private static final long serialVersionUID = -747696192479927491L;
	/**
	 * 用户第三方系统的唯一id
	 */
	private String uuid;
	/**
	 * 用户名
	 */
	private String username;
	/**
	 * 用户昵称
	 */
	private String nickname;
	/**
	 * 用户头像
	 */
	private String avatar;
	/**
	 * 用户网址
	 */
	private String blog;
	/**
	 * 所在公司
	 */
	private String company;
	/**
	 * 位置
	 */
	private String location;
	/**
	 * 用户邮箱
	 */
	private String email;
	/**
	 * 用户备注（各平台中的用户个人介绍）
	 */
	private String remark;
	/**
	 * 性别
	 */
	private AuthUserGender gender;
	/**
	 * 用户来源
	 */
	private String source;
	/**
	 * 用户授权的token信息
	 */
	private AuthToken token;
	/**
	 * 第三方平台返回的原始用户信息
	 */
	private JSONObject rawUserInfo;

	/**
	 * 联合登陆类型
	 */
	private ConnectAuthEnum connectEnum;

}
