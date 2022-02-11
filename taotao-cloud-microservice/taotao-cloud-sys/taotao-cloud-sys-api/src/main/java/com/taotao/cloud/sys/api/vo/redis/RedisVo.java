package com.taotao.cloud.sys.api.vo.redis;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;

public class RedisVo implements Serializable {

	@NotBlank
	private String key;

	@NotBlank
	private String value;

	public RedisVo() {
	}

	public RedisVo(String key, String value) {
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
