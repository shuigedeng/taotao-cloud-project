/*
 * Copyright 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.common.http;

import okhttp3.FormBody;

import javax.annotation.Nullable;
import java.util.Map;

/**
 * 表单构造器
 *
 */
public class FormBuilder {
	private final HttpRequest request;
	private final FormBody.Builder formBuilder;

	FormBuilder(HttpRequest request) {
		this.request = request;
		this.formBuilder = new FormBody.Builder();
	}

	public FormBuilder add(String name, @Nullable Object value) {
		this.formBuilder.add(name, HttpRequest.handleValue(value));
		return this;
	}

	public FormBuilder addMap(@Nullable Map<String, Object> formMap) {
		if (formMap != null && !formMap.isEmpty()) {
			formMap.forEach(this::add);
		}
		return this;
	}

	public FormBuilder addEncoded(String name, @Nullable Object encodedValue) {
		this.formBuilder.addEncoded(name, HttpRequest.handleValue(encodedValue));
		return this;
	}

	public HttpRequest build() {
		FormBody formBody = formBuilder.build();
		this.request.form(formBody);
		return this.request;
	}

	public Exchange execute() {
		return this.build().execute();
	}

	public AsyncExchange async() {
		return this.build().async();
	}
}
