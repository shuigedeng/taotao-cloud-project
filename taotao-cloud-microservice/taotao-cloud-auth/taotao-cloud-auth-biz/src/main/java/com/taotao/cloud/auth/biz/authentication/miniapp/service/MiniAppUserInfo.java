package com.taotao.cloud.auth.biz.authentication.miniapp.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MiniAppUserInfo {

	//OPEN_id
	private String openId;
	//会话密钥
	private String sessionKey;
	//头像路径
	private String avatarUrl;
	//城市
	private String city;
	//国家
	private String country;
	//性别
	private String gender;
	//语言
	private String language;
	//昵称
	private String nickName;
	//备注名或真实名
	private String realName;
	//省份
	private String province;
	private Integer stuId;
}
