/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

import okhttp3.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * BaseAuth
 *
 */
public class BaseAuthenticator implements Authenticator {
	private final String userName;
	private final String password;

	public BaseAuthenticator(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}

	@Override
	public Request authenticate(Route route, Response response) throws IOException {
		String credential = Credentials.basic(userName, password, StandardCharsets.UTF_8);
		return response.request().newBuilder()
			.header("Authorization", credential)
			.build();
	}
}
