/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.captcha.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * PointVO
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2021/8/24 16:47
 */
public class PointVO {

	private String secretKey;

	public int x;

	public int y;

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public PointVO(int x, int y, String secretKey) {
		this.secretKey = secretKey;
		this.x = x;
		this.y = y;
	}

	public PointVO() {
	}

	public PointVO(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public String toJsonString() {
		return String.format("{\"secretKey\":\"%s\",\"x\":%d,\"y\":%d}", secretKey, x, y);
	}

	public PointVO parse(String jsonStr) {
		Map<String, Object> m = new HashMap();
		Arrays.stream(jsonStr
			.replaceFirst(",\\{", "\\{")
			.replaceFirst("\\{", "")
			.replaceFirst("\\}", "")
			.replaceAll("\"", "")
			.split(",")).forEach(item -> {
			m.put(item.split(":")[0], item.split(":")[1]);
		});
		//PointVO d = new PointVO();
		setX(Double.valueOf("" + m.get("x")).intValue());
		setY(Double.valueOf("" + m.get("y")).intValue());
		setSecretKey(m.getOrDefault("secretKey", "") + "");
		return this;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PointVO pointVO = (PointVO) o;
		return x == pointVO.x && y == pointVO.y && Objects.equals(secretKey, pointVO.secretKey);
	}

	@Override
	public int hashCode() {

		return Objects.hash(secretKey, x, y);
	}
}
