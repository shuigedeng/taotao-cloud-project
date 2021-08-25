package com.taotao.cloud.security.jwt.model;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author zuihou
 * @date 2017-12-15 11:22
 */
public class Token implements Serializable {

	private static final long serialVersionUID = -8482946147572784305L;
	/**
	 * token
	 */
	private String token;
	/**
	 * 有效时间：单位：秒
	 */
	private Long expire;

	private LocalDateTime expiration;

	public Token() {
	}

	public Token(String token, Long expire, LocalDateTime expiration) {
		this.token = token;
		this.expire = expire;
		this.expiration = expiration;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Long getExpire() {
		return expire;
	}

	public void setExpire(Long expire) {
		this.expire = expire;
	}

	public LocalDateTime getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDateTime expiration) {
		this.expiration = expiration;
	}
}
