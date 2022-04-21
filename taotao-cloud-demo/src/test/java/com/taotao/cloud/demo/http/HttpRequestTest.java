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
import com.taotao.cloud.common.http.TrustAllHostNames;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

public class HttpRequestTest {

	public static void main(String[] args) {
		String json = HttpRequest.get("https://www.baidu.com/home/xman/data/tipspluslist")
			.useConsoleLog()
			.executeAsync()
			.join()
			.asString();
		System.out.println(json);
	}

	public static void sslTest() throws Exception {
		InputStream isTrustCa = HttpRequestTest.class.getResourceAsStream("/cert/ca.jks");
		InputStream isSelfCert = HttpRequestTest.class.getResourceAsStream("/cert/outgoing.CertwithKey.pkcs12");

		KeyStore selfCert = KeyStore.getInstance("pkcs12");
		selfCert.load(isSelfCert, "password".toCharArray());
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("sunx509");
		kmf.init(selfCert, "password".toCharArray());
		KeyStore caCert = KeyStore.getInstance("jks");
		caCert.load(isTrustCa, "caPassword".toCharArray());
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("sunx509");
		tmf.init(caCert);
		SSLContext sc = SSLContext.getInstance("TLS");

		TrustManager[] trustManagers = tmf.getTrustManagers();
		X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
		sc.init(kmf.getKeyManagers(), trustManagers, (SecureRandom) null);

		// 1. 全局配置证书
		OkHttpClient.Builder builder = new OkHttpClient.Builder()
			.sslSocketFactory(sc.getSocketFactory(), trustManager)
			.hostnameVerifier(TrustAllHostNames.INSTANCE);
		HttpRequest.setHttpClient(builder.build());

		// 2. 单次请求配置证书
		HttpRequest.get("https://123.xxx")
			.useConsoleLog(LogLevel.BODY)
			.sslSocketFactory(sc.getSocketFactory(), trustManager)
			.disableSslValidation()
			.execute()
			.asString();
	}
}
