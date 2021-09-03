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
package com.taotao.cloud.core.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.taotao.cloud.common.utils.JsonUtil;
import com.taotao.cloud.core.model.Callable;
import com.taotao.cloud.core.properties.HttpClientProperties;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
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
	 * DEFAULT
	 */
	public static DefaultHttpClient DEFAULT;
	/**
	 * initParams
	 */
	private InitMap initParams;
	/**
	 * manager
	 */
	private PoolingHttpClientConnectionManager manager;
	/**
	 * client
	 */
	private CloseableHttpClient client;

	static {
		initDefault();
	}

	private DefaultHttpClient(InitMap initParams) {
		this.initParams = this.initDefaultParams(initParams);
	}

	/**
	 * initDefault
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:10:22
	 */
	public static void initDefault() {
		DEFAULT = DefaultHttpClient.create("taotao.cloud.core.http.httpclient",
			HttpClientProperties.toMap());
	}

	/**
	 * create
	 *
	 * @param httpClientId httpClientId
	 * @param initParams   initParams
	 * @return {@link DefaultHttpClient }
	 * @author shuigedeng
	 * @since 2021-09-02 20:11:08
	 */
	public static DefaultHttpClient create(String httpClientId, InitMap initParams) {
		return HttpClientManager.DEFAULT.register(httpClientId, new DefaultHttpClient(initParams));
	}


	/**
	 * initDefaultParams
	 *
	 * @param initParams initParams
	 * @return {@link InitMap }
	 * @author shuigedeng
	 * @since 2021-09-02 20:11:35
	 */
	private InitMap initDefaultParams(InitMap initParams) {
		if (initParams == null) {
			initParams = new InitMap();
		}
		for (EnumHttpConnectParam v : EnumHttpConnectParam.values()) {
			initParams.trySetDefaultParams(v, v.getDefaultValue());
		}
		return initParams;
	}

	/**
	 * open
	 *
	 * @author shuigedeng
	 * @since 2021-09-02 20:11:38
	 */
	public void open() {
		Registry registry =
			RegistryBuilder
				.create()
				.register("http", PlainConnectionSocketFactory.INSTANCE)
				.register("https", SSLConnectionSocketFactory.getSystemSocketFactory())
				.build();

		//HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connectionFactory =
		//        new ManagedHttpClientConnectionFactory(DefaultHttpRequestWriterFactory.INSTANCE, DefaultHttpResponseParserFactory.INSTANCE);
		//DnsResolver dnsResolver = SystemDefaultDnsResolver.INSTANCE;

		manager = new PoolingHttpClientConnectionManager(registry);

		//默认为Socket配置
		SocketConfig defaultSocketConfig = SocketConfig.custom()
			//tcp 包延迟优化,true
			.setTcpNoDelay(initParams.getParams("TcpNoDelay", boolean.class))
			.build();

		manager.setDefaultSocketConfig(defaultSocketConfig);
		//设置整个连接池的最大连接数,500
		manager.setMaxTotal(initParams.getParams("MaxTotal", int.class));
		//每个路由的默认最大连接，每个路由实际最大连接数由DefaultMaxPerRoute控制，而MaxTotal是整个池子的最大数 500
		// 设置过小无法支持大并发(ConnectionPoolTimeoutException) Timeout waiting for connection from pool
		manager.setDefaultMaxPerRoute(initParams.getParams("DefaultMaxPerRoute", int.class));
		// 每个路由的最大连接数
		// 在从连接池获取连接时，连接不活跃多长时间后需要进行一次验证，默认为2s,默认设置 5*1000
		manager.setValidateAfterInactivity(
			initParams.getParams("ValidateAfterInactivity", int.class));

		//默认请求配置
		RequestConfig defaultRequestConfig = RequestConfig.custom()
			//设置连接超时时间，2s,2*1000
			.setConnectTimeout(initParams.getParams("ConnectTimeout", int.class))
			//设置等待数据超时时间，5s 5*1000
			.setSocketTimeout(initParams.getParams("SocketTimeout", int.class))
			//设置从连接池获取连接的等待超时时间,2000
			.setConnectionRequestTimeout(
				initParams.getParams("ConnectionRequestTimeout", int.class))
			.build();

		//创建
		HttpClientBuilder httpClientBuilder = HttpClients
			.custom()
			//连接池不是共享模式,true
			.setConnectionManagerShared(
				initParams.getParams("ConnectionManagerShared", boolean.class));

		//定期回收空闲连接 60
		httpClientBuilder = httpClientBuilder.evictIdleConnections(
			initParams.getParams("EvictIdleConnectionsTime", int.class), TimeUnit.SECONDS);
		if (initParams.getParams("IsEvictExpiredConnections", boolean.class)) {
			//定期回收过期连接 true
			httpClientBuilder = httpClientBuilder.evictExpiredConnections();
		}
		if (initParams.getParams("ConnectionTimeToLive", int.class) > 0) {
			//连接存活时间，如果不设置，则根据长连接信息决定 60
			httpClientBuilder = httpClientBuilder.setConnectionTimeToLive(
				initParams.getParams("ConnectionTimeToLive", int.class), TimeUnit.SECONDS);
		}
		if (initParams.getParams("RetryCount", int.class) > 0) {
			//设置重试次数，默认是3次，当前是禁用掉（根据需要开启） 0
			httpClientBuilder = httpClientBuilder.setRetryHandler(
				new DefaultHttpRequestRetryHandler(initParams.getParams("RetryCount", int.class),
					false));
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
	 * @author shuigedeng
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
	 * @author shuigedeng
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
	 * @author shuigedeng
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
	 * @author shuigedeng
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
	 * @author shuigedeng
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

	//@Override
	public String patch(String uri, Params params) {
		return toString(patch(uri, params, response -> response));
	}

	//@Override
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
	 * @author shuigedeng
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
	 * @author shuigedeng
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
	 * @author shuigedeng
	 * @since 2021-09-02 20:12:25
	 */
	private HttpGet getGet(String uri, Params params) {
		HttpGet httpGet = new HttpGet(uri);
		Iterator headers = params.getHeaders().iterator();

		while (headers.hasNext()) {
			Header header = (Header) headers.next();
			httpGet.setHeader(header);
		}

		String query = URI.create(uri).getQuery();
		List<NameValuePair> pairs = new ArrayList<>();
		if (query != null) {
			pairs = URLEncodedUtils.parse(query, Charsets.UTF_8);
		}

		if (((List) pairs).size() == 0) {
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
	 * @author shuigedeng
	 * @since 2021-09-02 20:12:28
	 */
	private HttpPost getPost(String uri, Params params) {
		HttpPost httpPost = new HttpPost(uri);
		Iterator headers = params.getHeaders().iterator();

		while (headers.hasNext()) {
			Header header = (Header) headers.next();
			httpPost.setHeader(header);
		}

		if (params.getContentType() == null
			|| params.getContentType() != null && params.getContentType()
			.equals(ContentType.DEFAULT_TEXT)) {
			params.setContentType(
				ContentType.create("application/x-www-form-urlencoded", Charsets.UTF_8));
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
	 * @author shuigedeng
	 * @since 2021-09-02 20:12:32
	 */
	private HttpPut getPut(String uri, Params params) {
		HttpPut httpPut = new HttpPut(uri);
		Iterator headers = params.getHeaders().iterator();

		while (headers.hasNext()) {
			Header header = (Header) headers.next();
			httpPut.addHeader(header);
		}

		if (params.getContentType() == null
			|| params.getContentType() != null && params.getContentType()
			.equals(ContentType.DEFAULT_TEXT)) {
			params.setContentType(
				ContentType.create("application/x-www-form-urlencoded", Charsets.UTF_8));
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
	 * @author shuigedeng
	 * @since 2021-09-02 20:12:38
	 */
	private HttpPatch getPatch(String uri, Params params) {
		HttpPatch httpPatch = new HttpPatch(uri);
		Iterator headers = params.getHeaders().iterator();

		while (headers.hasNext()) {
			Header header = (Header) headers.next();
			httpPatch.addHeader(header);
		}

		if (params.getContentType() == null
			|| params.getContentType() != null && params.getContentType()
			.equals(ContentType.DEFAULT_TEXT)) {
			params.setContentType(
				ContentType.create("application/x-www-form-urlencoded", Charsets.UTF_8));
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
	 * @author shuigedeng
	 * @since 2021-09-02 20:12:41
	 */
	private HttpDelete getDelete(String uri, Params params) {
		HttpDelete httpDelete = new HttpDelete(uri);
		Iterator headers = params.getHeaders().iterator();

		while (headers.hasNext()) {
			Header header = (Header) headers.next();
			httpDelete.addHeader(header);
		}

		String query = URI.create(uri).getQuery();
		List<NameValuePair> pairs = new ArrayList<>();
		if (query != null) {
			pairs = URLEncodedUtils.parse(query, Charsets.UTF_8);
		}

		if (((List) pairs).size() == 0) {
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
	 * @author shuigedeng
	 * @since 2021-09-02 20:12:44
	 */
	public String toString(CloseableHttpResponse response) {
		try {
			return EntityUtils.toString(response.getEntity());
		} catch (IOException e) {
			throw new HttpException(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * endStream
	 *
	 * @param response response
	 * @author shuigedeng
	 * @since 2021-09-02 20:12:46
	 */
	public void endStream(CloseableHttpResponse response) {
		try {
			if (response.getEntity() != null) {
				response.getEntity().getContent().close();
			}
		} catch (IOException e) {
			throw new HttpException(e);
		} finally {
			try {
				response.close();
			} catch (IOException e) {
			}
		}
	}

	/**
	 * isClose
	 *
	 * @return boolean
	 * @author shuigedeng
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
				this.client.getConnectionManager().shutdown();
				this.client = null;
			}
		} catch (Exception e) {
			exception = e;
		}
		//从连接管理中移除
		try {
			HttpClientManager.DEFAULT.remove(this);
		} catch (Exception e) {
			exception = e;
		}
		if (exception != null) {
			throw new HttpException(exception);
		}
	}

	public PoolingHttpClientConnectionManager getManager() {
		return manager;
	}

	public void setManager(PoolingHttpClientConnectionManager manager) {
		this.manager = manager;
	}

	public CloseableHttpClient getClient() {
		return client;
	}

	public void setClient(CloseableHttpClient client) {
		this.client = client;
	}


	public InitMap getInitParams() {
		return initParams;
	}

	public void setInitParams(InitMap initParams) {
		this.initParams = initParams;
	}
}
