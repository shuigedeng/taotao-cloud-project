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
package com.taotao.cloud.core.endpoint;

import cn.hutool.json.JSONObject;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.Selector;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;

/**
 * TaoTaoCloudEndPoint
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:08:52
 */
@Endpoint(id = "taotao-cloud")
public class TaoTaoCloudEndPoint {

	private String STATUS = "up";
	private String DETAIL = "一切正常";

	@ReadOperation
	public JSONObject test() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", STATUS);
		jsonObject.put("detail", DETAIL);
		return jsonObject;
	}

	@ReadOperation
	public JSONObject testSelector(@Selector String name) {
		JSONObject jsonObject = new JSONObject();
		if ("status".equals(name)) {
			jsonObject.put("status", STATUS);
		} else if ("detail".equals(name)) {
			jsonObject.put("detail", DETAIL);
		}
		return jsonObject;
	}

	//动态修改指标
	@WriteOperation
	public void test4(@Selector String name, @Nullable String value) {
		if (!StringUtils.isEmpty(value)) {
			if ("status".equals(name)) {
				STATUS = value;
			} else if ("detail".equals(name)) {
				DETAIL = value;
			}
		}
	}
}
