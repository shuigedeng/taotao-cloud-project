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
package com.taotao.cloud.core.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.taotao.cloud.common.model.Callable;
import com.taotao.cloud.common.utils.common.JsonUtil;
import com.taotao.cloud.core.properties.HttpClientProperties;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import okhttp3.MediaType;
import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.DnsResolver;
import org.apache.http.conn.HttpConnectionFactory;
import org.apache.http.conn.ManagedHttpClientConnection;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.DefaultHttpResponseParserFactory;
import org.apache.http.impl.conn.ManagedHttpClientConnectionFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;
import org.apache.http.impl.io.DefaultHttpRequestWriterFactory;
import org.apache.http.util.EntityUtils;

/**
 * DefaultHttpClient
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:09:34
 */
public class DefaultHttpClient implements HttpClient {

	/**
	 * initParams
	 */
	private HttpClientProperties httpClientProperties;
	/**
	 * manager
	 */
	private PoolingHttpClientConnectionManager manager;
	/**
	 * client
	 */
	private CloseableHttpClient client;

	private HttpClientManager httpClientManager;

	public DefaultHttpClient() {

	}

	public DefaultHttpClient(HttpClientProperties httpClientProperties,
		HttpClientManager httpClientManager) {
		this.httpClientProperties = httpClientProperties;
		this.httpClientManager = httpClientManager;
	}

	/**
	 * open
	 *
	 * @since 2021-09-02 20:11:38
	 */
	@Override
	public void open() {
		RegistryBuilder<ConnectionSocketFactory> builder = RegistryBuilder.create();
		Registry<ConnectionSocketFactory> registry = builder
			.register("http", PlainConnectionSocketFactory.INSTANCE)
			.register("https", SSLConnectionSocketFactory.getSystemSocketFactory())
			.build();

		HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory =
			new ManagedHttpClientConnectionFactory(DefaultHttpRequestWriterFactory.INSTANCE,
				DefaultHttpResponseParserFactory.INSTANCE);
		DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

		manager = new PoolingHttpClientConnectionManager(registry, connectionFactory, dnsResolver);

		//默认为Socket配置
		SocketConfig defaultSocketConfig = SocketConfig.custom()
			//tcp 包延迟优化,true
			.setTcpNoDelay(httpClientProperties.getTcpNoDelay())
			.build();

		manager.setDefaultSocketConfig(defaultSocketConfig);
		//设置整个连接池的最大连接数,500
		manager.setMaxTotal(httpClientProperties.getMaxTotal());
		//每个路由的默认最大连接，每个路由实际最大连接数由DefaultMaxPerRoute控制，而MaxTotal是整个池子的最大数 500
		// 设置过小无法支持大并发(ConnectionPoolTimeoutException) Timeout waiting for connection from pool
		manager.setDefaultMaxPerRoute(httpClientProperties.getDefaultMaxPerRoute());
		// 每个路由的最大连接数
		// 在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为2s,默认设置 5*1000
		manager.setValidateAfterInactivity(httpClientProperties.getValidateAfterInactivity());

		//默认请求配置
		RequestConfig defaultRequestConfig = RequestConfig.custom()
			//设置连接超时时间，2s,2*1000
			.setConnectTimeout(httpClientProperties.getConnectTimeout())
			//设置等待数据超时时间，5s 5*1000
			.setSocketTimeout(httpClientProperties.getSocketTimeout())
			//设置从连接池获取连接的等待超时时间,2000
			.setConnectionRequestTimeout(httpClientProperties.getConnectionRequestTimeout())
			.build();

		//创建
		HttpClientBuilder httpClientBuilder = HttpClients
			.custom()
			//连接池不是共享模式,true
			.setConnectionManagerShared(httpClientProperties.isConnectionManagerShared());

		//定期回收空闲连接 60
		httpClientBuilder.evictIdleConnections(
			httpClientProperties.getEvictIdleConnectionsTime(), TimeUnit.SECONDS);
		if (httpClientProperties.isEvictExpiredConnections()) {
			//定期回收过期连接 true
			httpClientBuilder.evictExpiredConnections();
		}

		if (httpClientProperties.getConnectionTimeToLive() > 0) {
			//连接存活时间，如果不设置，则根据长连接信息决定 60
			httpClientBuilder.setConnectionTimeToLive(
				httpClientProperties.getConnectionTimeToLive(), TimeUnit.SECONDS);
		}

		if (httpClientProperties.getRetryCount() > 0) {
			//设置重试次数，默认是3次，当前是禁用掉（根据需要开启） 0
			httpClientBuilder.setRetryHandler(
				new DefaultHttpRequestRetryHandler(httpClientProperties.getRetryCount(), false));
		}

		//设置默认请求配置
		this.client = httpClientBuilder
			.setDefaultRequestConfig(defaultRequestConfig)
			.setConnectionManager(manager)
			//连接重用策略，即是否能keepAlive
			.setConnectionReuseStrategy(DefaultConnectionReuseStrategy.INSTANCE)
			//长连接配置，即获取长连接生产多长时间
			.setKeepAliveStrategy(DefaultConnectionKeepAliveStrategy.INSTANCE)
			.build();
	}

	/**
	 * checkUri
	 *
	 * @param uri uri
	 * @since 2021-09-02 20:11:52
	 */
	private void checkUri(String uri) {
		if (uri == null) {
			throw new HttpException("Uri cannot be null");
		}
	}

	/**
	 * checkParmas
	 *
	 * @param params params
	 * @since 2021-09-02 20:11:55
	 */
	private void checkParmas(Params params) {
		if (params == null) {
			throw new HttpException("Params cannot be null");
		}
	}

	/**
	 * get
	 *
	 * @param uri    uri
	 * @param params params
	 * @param action action
	 * @return {@link CloseableHttpResponse }
	 * @since 2021-09-02 20:11:58
	 */
	public CloseableHttpResponse get(String uri, Params params,
		Callable.Func1<CloseableHttpResponse, CloseableHttpResponse> action) {
		checkUri(uri);
		HttpGet httpGet;
		if (params != null) {
			checkParmas(params);
			httpGet = this.getGet(uri, params);
		} else {
			httpGet = new HttpGet(uri);
		}
		try {
			CloseableHttpResponse response = this.client.execute(httpGet);
			return action.invoke(response);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public String get(String uri) {
		return toString(get(uri, null, response -> response));
	}

	@Override
	public String get(String uri, Params params) {
		return toString(get(uri, params, response -> response));
	}

	@Override
	public <T> T get(String uri, TypeReference<T> ref) {
		String rsp = this.get(uri);
		return JsonUtil.toObject(rsp, ref);
	}

	@Override
	public <T> T get(String uri, Params params, TypeReference<T> ref) {
		String rsp = this.get(uri, params);
		return JsonUtil.toObject(rsp, ref);
	}

	/**
	 * post
	 *
	 * @param uri    uri
	 * @param params params
	 * @param action action
	 * @return {@link CloseableHttpResponse }
	 * @since 2021-09-02 20:12:04
	 */
	public CloseableHttpResponse post(String uri, Params params,
		Callable.Func1<CloseableHttpResponse, CloseableHttpResponse> action) {
		checkUri(uri);
		HttpPost httpPost;
		if (params != null) {
			checkParmas(params);
			httpPost = this.getPost(uri, params);
		} else {
			httpPost = new HttpPost(uri);
		}

		try {
			CloseableHttpResponse response = this.client.execute(httpPost);
			return action.invoke(response);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public String post(String uri, Params params) {
		return toString(post(uri, params, response -> response));
	}

	@Override
	public <T> T post(String uri, Params params, TypeReference<T> ref) {
		String rsp = this.post(uri, params);
		return JsonUtil.toObject(rsp, ref);
	}

	/**
	 * patch
	 *
	 * @param uri    uri
	 * @param params params
	 * @param action action
	 * @return {@link CloseableHttpResponse }
	 * @since 2021-09-02 20:12:10
	 */
	public CloseableHttpResponse patch(String uri, Params params,
		Callable.Func1<CloseableHttpResponse, CloseableHttpResponse> action) {
		checkUri(uri);
		HttpPatch httpPatch;
		if (params != null) {
			checkParmas(params);
			httpPatch = this.getPatch(uri, params);
		} else {
			httpPatch = new HttpPatch(uri);
		}
		try {
			CloseableHttpResponse response = this.client.execute(httpPatch);
			return action.invoke(response);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	public String patch(String uri, Params params) {
		return toString(patch(uri, params, response -> response));
	}

	public <T> T patch(String uri, Params params, TypeReference<T> ref) {
		String rsp = this.put(uri, params);
		return JsonUtil.toObject(rsp, ref);
	}

	/**
	 * put
	 *
	 * @param uri    uri
	 * @param params params
	 * @param action action
	 * @return {@link CloseableHttpResponse }
	 * @since 2021-09-02 20:12:16
	 */
	public CloseableHttpResponse put(String uri, Params params,
		Callable.Func1<CloseableHttpResponse, CloseableHttpResponse> action) {
		checkUri(uri);
		HttpPut httpPut;
		if (params != null) {
			checkParmas(params);
			httpPut = this.getPut(uri, params);
		} else {
			httpPut = new HttpPut(uri);
		}
		try {
			CloseableHttpResponse response = this.client.execute(httpPut);
			return action.invoke(response);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public String put(String uri, Params params) {
		return toString(put(uri, params, response -> response));
	}

	@Override
	public <T> T put(String uri, Params params, TypeReference<T> ref) {
		String rsp = this.put(uri, params);
		return JsonUtil.toObject(rsp, ref);
	}

	/**
	 * delete
	 *
	 * @param uri    uri
	 * @param params params
	 * @param action action
	 * @return {@link CloseableHttpResponse }
	 * @since 2021-09-02 20:12:20
	 */
	public CloseableHttpResponse delete(String uri, Params params,
		Callable.Func1<CloseableHttpResponse, CloseableHttpResponse> action) {
		checkUri(uri);
		HttpDelete httpDelete;
		if (params != null) {
			checkParmas(params);
			httpDelete = this.getDelete(uri, params);
		} else {
			httpDelete = new HttpDelete(uri);
		}
		try {
			CloseableHttpResponse response = this.client.execute(httpDelete);
			return action.invoke(response);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public String delete(String uri) {
		return toString(delete(uri, null, response -> response));
	}

	@Override
	public <T> T delete(String uri, TypeReference<T> ref) {
		String rsp = this.delete(uri);
		return JsonUtil.toObject(rsp, ref);
	}

	@Override
	public String delete(String uri, Params params) {
		return toString(delete(uri, params, response -> response));
	}

	@Override
	public <T> T delete(String uri, Params params, TypeReference<T> ref) {
		String rsp = this.delete(uri, params);
		return JsonUtil.toObject(rsp, ref);
	}

	/**
	 * getGet
	 *
	 * @param uri    uri
	 * @param params params
	 * @return {@link HttpGet }
	 * @since 2021-09-02 20:12:25
	 */
	private HttpGet getGet(String uri, Params params) {
		HttpGet httpGet = new HttpGet(uri);

		for (Header header : params.getHeaders()) {
			httpGet.setHeader(header);
		}

		String query = URI.create(uri).getQuery();
		List<NameValuePair> pairs = new ArrayList<>();
		if (query != null) {
			pairs = URLEncodedUtils.parse(query, StandardCharsets.UTF_8);
		}

		if (pairs.size() == 0) {
			uri = uri + "?";
		} else {
			uri = uri + "&";
		}

		String data = params.toString();
		if (data.length() > 0) {
			uri = uri + data;
		}

		httpGet.setURI(URI.create(uri));
		return httpGet;
	}

	/**
	 * getPost
	 *
	 * @param uri    uri
	 * @param params params
	 * @return {@link HttpPost }
	 * @since 2021-09-02 20:12:28
	 */
	private HttpPost getPost(String uri, Params params) {
		HttpPost httpPost = new HttpPost(uri);

		for (Header header : params.getHeaders()) {
			httpPost.setHeader(header);
		}

		if (params.getContentType() == null
			|| params.getContentType() != null && params.getContentType()
			.equals(ContentType.DEFAULT_TEXT)) {
			params.setContentType(
				ContentType.create("application/x-www-form-urlencoded", StandardCharsets.UTF_8));
		}

		httpPost.setEntity(params.toEntity());
		return httpPost;
	}

	/**
	 * getPut
	 *
	 * @param uri    uri
	 * @param params params
	 * @return {@link HttpPut }
	 * @since 2021-09-02 20:12:32
	 */
	private HttpPut getPut(String uri, Params params) {
		HttpPut httpPut = new HttpPut(uri);

		for (Header header : params.getHeaders()) {
			httpPut.addHeader(header);
		}

		if (params.getContentType() == null
			|| params.getContentType() != null && params.getContentType()
			.equals(ContentType.DEFAULT_TEXT)) {
			params.setContentType(
				ContentType.create("application/x-www-form-urlencoded", StandardCharsets.UTF_8));
		}

		httpPut.setEntity(params.toEntity());
		return httpPut;
	}

	/**
	 * getPatch
	 *
	 * @param uri    uri
	 * @param params params
	 * @return {@link HttpPatch }
	 * @since 2021-09-02 20:12:38
	 */
	private HttpPatch getPatch(String uri, Params params) {
		HttpPatch httpPatch = new HttpPatch(uri);

		for (Header header : params.getHeaders()) {
			httpPatch.addHeader(header);
		}

		if (params.getContentType() == null
			|| params.getContentType() != null && params.getContentType()
			.equals(ContentType.DEFAULT_TEXT)) {
			params.setContentType(
				ContentType.create("application/x-www-form-urlencoded", StandardCharsets.UTF_8));
		}

		httpPatch.setEntity(params.toEntity());
		return httpPatch;
	}


	/**
	 * getDelete
	 *
	 * @param uri    uri
	 * @param params params
	 * @return {@link HttpDelete }
	 * @since 2021-09-02 20:12:41
	 */
	private HttpDelete getDelete(String uri, Params params) {
		HttpDelete httpDelete = new HttpDelete(uri);

		for (Header header : params.getHeaders()) {
			httpDelete.addHeader(header);
		}

		String query = URI.create(uri).getQuery();
		List<NameValuePair> pairs = new ArrayList<>();
		if (query != null) {
			pairs = URLEncodedUtils.parse(query, StandardCharsets.UTF_8);
		}

		if (((List<?>) pairs).size() == 0) {
			uri = uri + "?";
		} else {
			uri = uri + "&";
		}

		String data = params.toString();
		if (data.length() > 0) {
			uri = uri + data;
		}

		httpDelete.setURI(URI.create(uri));
		return httpDelete;
	}

	/**
	 * toString
	 *
	 * @param response response
	 * @return {@link String }
	 * @since 2021-09-02 20:12:44
	 */
	public String toString(CloseableHttpResponse response) {
		try (response) {
			return EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	/**
	 * endStream
	 *
	 * @param response response
	 * @since 2021-09-02 20:12:46
	 */
	public void endStream(CloseableHttpResponse response) {
		try (response) {
			if (response.getEntity() != null) {
				response.getEntity().getContent().close();
			}
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	/**
	 * isClose
	 *
	 * @return boolean
	 * @since 2021-09-02 20:12:49
	 */
	public boolean isClose() {
		return this.manager == null && this.client == null;
	}

	@Override
	public void close() {
		//释放连接池
		Exception exception = null;
		try {
			if (this.manager != null) {
				this.manager.close();
				this.manager = null;
			}
		} catch (Exception e) {
			exception = e;
		}

		//释放client
		try {
			if (this.client != null) {
				this.client.close();
				getManager().shutdown();
				this.client = null;
			}
		} catch (Exception e) {
			exception = e;
		}

		//从连接管理中移除
		try {
			this.httpClientManager.remove(this);
		} catch (Exception e) {
			exception = e;
		}

		if (exception != null) {
			throw new HttpException(exception);
		}
	}

	@Override
	public PoolingHttpClientConnectionManager getManager() {
		return manager;
	}

	public void setManager(PoolingHttpClientConnectionManager manager) {
		this.manager = manager;
	}

	@Override
	public CloseableHttpClient getClient() {
		return client;
	}

	public void setClient(CloseableHttpClient client) {
		this.client = client;
	}

}
