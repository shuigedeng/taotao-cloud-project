package com.taotao.cloud.oauth2.biz.controller;


import java.time.LocalDateTime;

public class AuthUserOauth2 {

	private Integer id;
	private String clientRegistrationId;
	private String principalName;
	private String nickname;
	private String avatar;
	private String userId;
	private LocalDateTime createdAt;

	public AuthUserOauth2() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClientRegistrationId() {
		return clientRegistrationId;
	}

	public void setClientRegistrationId(String clientRegistrationId) {
		this.clientRegistrationId = clientRegistrationId;
	}

	public String getPrincipalName() {
		return principalName;
	}

	public void setPrincipalName(String principalName) {
		this.principalName = principalName;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public AuthUserOauth2(Integer id, String clientRegistrationId, String principalName,
		String nickname, String avatar, String userId, LocalDateTime createdAt) {
		this.id = id;
		this.clientRegistrationId = clientRegistrationId;
		this.principalName = principalName;
		this.nickname = nickname;
		this.avatar = avatar;
		this.userId = userId;
		this.createdAt = createdAt;
	}
}
