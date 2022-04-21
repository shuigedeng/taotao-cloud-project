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

package com.taotao.cloud.demo.http;

import com.taotao.cloud.common.http.HttpRequest;
import com.taotao.cloud.common.http.LogLevel;
import org.junit.Test;

import java.io.IOException;

public class HttpRequestProxyTest {

	@Test(expected = IOException.class)
	public void test1() {
		// 代理都不可用
		HttpRequest.get("https://www.baidu.com")
			.useConsoleLog(LogLevel.BASIC)
			.retry()
			.proxySelector(new MicaProxySelector())
			.execute()
			.asString();
	}
}
