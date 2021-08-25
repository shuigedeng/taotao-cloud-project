package com.taotao.cloud.security.jwt.model;

import java.io.Serializable;

/**
 * jwt 存储的 内容
 *
 * @author zuihou
 * @date 2018/11/20
 */
public class JwtUserInfo implements Serializable {

	/**
	 * 账号id
	 */
	private Long userId;
	/**
	 * 账号
	 */
	private String account;
	/**
	 * 姓名
	 */
	private String name;

	public JwtUserInfo() {
	}

	public JwtUserInfo(Long userId, String account, String name) {
		this.userId = userId;
		this.account = account;
		this.name = name;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
