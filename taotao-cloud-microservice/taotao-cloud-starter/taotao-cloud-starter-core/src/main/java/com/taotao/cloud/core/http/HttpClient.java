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
import com.taotao.cloud.common.utils.common.JsonUtil;
import java.io.Closeable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.codec.Charsets;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

/**
 * HttpClient
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-02 20:15:08
 */
public interface HttpClient extends Closeable {

	/**
	 * get
	 *
	 * @param url url
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:16:20
	 */
	String get(String url);

	/**
	 * get
	 *
	 * @param url    url
	 * @param params params
	 * @return {@link java.lang.String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:16:23
	 */
	String get(String url, Params params);

	/**
	 * get
	 *
	 * @param url            url
	 * @param tTypeReference tTypeReference
	 * @param <T>            T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:16:26
	 */
	<T> T get(String url, TypeReference<T> tTypeReference);

	/**
	 * get
	 *
	 * @param url            url
	 * @param params         params
	 * @param tTypeReference tTypeReference
	 * @param <T>            T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:16:35
	 */
	<T> T get(String url, Params params, TypeReference<T> tTypeReference);

	/**
	 * post
	 *
	 * @param url    url
	 * @param params params
	 * @return {@link String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:16:44
	 */
	String post(String url, Params params);

	/**
	 * post
	 *
	 * @param url            url
	 * @param params         params
	 * @param tTypeReference tTypeReference
	 * @param <T>            T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:16:48
	 */
	<T> T post(String url, Params params, TypeReference<T> tTypeReference);

	/**
	 * put
	 *
	 * @param url    url
	 * @param params params
	 * @return {@link String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:16:57
	 */
	String put(String url, Params params);

	/**
	 * put
	 *
	 * @param url            url
	 * @param params         params
	 * @param tTypeReference tTypeReference
	 * @param <T>            T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:17:00
	 */
	<T> T put(String url, Params params, TypeReference<T> tTypeReference);

	/**
	 * delete
	 *
	 * @param url url
	 * @return {@link String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:17:07
	 */
	String delete(String url);

	/**
	 * delete
	 *
	 * @param url            url
	 * @param tTypeReference tTypeReference
	 * @param <T>            T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:17:09
	 */
	<T> T delete(String url, TypeReference<T> tTypeReference);

	/**
	 * delete
	 *
	 * @param url    url
	 * @param params params
	 * @return {@link String }
	 * @author shuigedeng
	 * @since 2021-09-02 20:17:17
	 */
	String delete(String url, Params params);

	/**
	 * delete
	 *
	 * @param url            url
	 * @param params         params
	 * @param tTypeReference tTypeReference
	 * @param <T>            T
	 * @return T
	 * @author shuigedeng
	 * @since 2021-09-02 20:17:25
	 */
	<T> T delete(String url, Params params, TypeReference<T> tTypeReference);

	/**
	 * getClient
	 *
	 * @return {@link CloseableHttpClient }
	 * @author shuigedeng
	 * @since 2021-12-01 15:09:44
	 */
	CloseableHttpClient getClient();

	/**
	 * getManager
	 *
	 * @return {@link PoolingHttpClientConnectionManager }
	 * @author shuigedeng
	 * @since 2021-12-01 15:10:12
	 */
	PoolingHttpClientConnectionManager getManager();

	/**
	 * open
	 *
	 * @author shuigedeng
	 * @since 2021-12-01 15:11:45
	 */
	public void open();

	/**
	 * 请求参数
	 *
	 * @author shuigedeng
	 * @version 2021.9
	 * @since 2021-09-02 20:18:16
	 */
	class Params {

		/**
		 * headers
		 */
		private List<Header> headers;
		/**
		 * data
		 */
		private Map<String, Object> data;
		/**
		 * bodyMultimap
		 */
		private Map<String, Collection<ContentBody>> bodyMultimap;
		/**
		 * contentType
		 */
		private ContentType contentType;

		private Params() {
			this.headers = new ArrayList<>();
			this.contentType = ContentType.DEFAULT_TEXT;
		}

		public static Builder custom() {
			return new Builder();
		}

		public List<Header> getHeaders() {
			return this.headers;
		}

		public void setHeaders(List<Header> headers) {
			this.headers = headers;
		}

		public ContentType getContentType() {
			return this.contentType;
		}

		public void setContentType(ContentType contentType) {
			this.contentType = contentType;
		}

		@Override
		public String toString() {
			if (this.contentType == ContentType.APPLICATION_JSON) {
				return JsonUtil.toJSONString(this.data);
			} else {
				List<NameValuePair> tmp = new ArrayList<>();

				for (Map.Entry<String, Object> entry : this.data.entrySet()) {
					tmp.add(new BasicNameValuePair(entry.getKey(),
						entry.getValue().toString()));
				}

				return URLEncodedUtils.format(tmp, Charsets.UTF_8);
			}
		}

		/**
		 * toEntity
		 *
		 * @return {@link org.apache.http.HttpEntity }
		 * @author shuigedeng
		 * @since 2021-09-02 20:18:58
		 */
		public HttpEntity toEntity() {
			if (!this.contentType.equals(ContentType.MULTIPART_FORM_DATA)) {
				return EntityBuilder.create().setContentType(this.contentType)
					.setContentEncoding("utf-8").setText(this.toString()).build();
			} else {
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();

				for (String key : this.data.keySet()) {
					Object value = this.data.get(key);

					try {
						builder.addPart(key, new StringBody(value.toString(),
							ContentType.APPLICATION_FORM_URLENCODED));
					} catch (Exception var8) {
						throw new HttpException(var8);
					}
				}

				Map<String, Collection<ContentBody>> items = this.bodyMultimap;

				for (String key : items.keySet()) {
					Collection<ContentBody> value = items.get(key);

					for (ContentBody contentBody : value) {
						builder.addPart(key, contentBody);
					}
				}

				return builder.build();
			}
		}

		public static class Builder {

			private final Map<String, Object> data = new HashMap<>();
			private final Map<String, Collection<ContentBody>> bodyMultimap = new HashMap<>();
			private final List<Header> headers = new ArrayList<>();
			private ContentType contentType;

			public Builder() {
			}

			public Builder header(String k, String v) {
				this.headers.add(new BasicHeader(k, v));
				return this;
			}

			public Builder add(Object object) {
				try {
					for (Field field : object.getClass().getDeclaredFields()) {
						if (!field.isAccessible()) {
							field.setAccessible(true);
						}
						this.data.put(field.getName(), field.get(object));
					}
				} catch (Exception e) {
					throw new HttpException(e);
				}
				return this;
			}

			public Builder add(Map<String, Object> params) {
				this.data.putAll(params);
				return this;
			}

			public Builder add(String k, Object v) {
				if (k != null && v != null) {
					this.data.put(k, v);
					return this;
				}

				throw new IllegalArgumentException("The specified k or v cannot be null");
			}

			public Builder addContentBody(String k, ContentBody contentBody) {
				if (contentBody == null) {
					throw new IllegalArgumentException("The specified content body cannot be null");
				}
				if (!this.bodyMultimap.containsKey(k)) {
					this.bodyMultimap.put(k, new ArrayList<>());
				}
				this.bodyMultimap.get(k).add(contentBody);
				return this;
			}

			public Builder setContentType(ContentType contentType) {
				this.contentType = contentType;
				return this;
			}

			public Params build() {
				Params params = new Params();
				params.headers = this.headers;
				params.contentType = this.contentType;
				params.data = this.data;
				params.bodyMultimap = this.bodyMultimap;

				return params;
			}
		}
	}
}
