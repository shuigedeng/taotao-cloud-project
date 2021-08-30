package com.taotao.cloud.core.http;

import com.fasterxml.jackson.core.type.TypeReference;
import com.taotao.cloud.common.utils.BeanUtil;
import com.taotao.cloud.common.utils.JsonUtil;
import java.io.Closeable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.util.StringUtils;

/**
 * Created by yanglikai on 2019/5/23. by chejiangyi
 */
public interface HttpClient extends Closeable {

	String get(String var1);

	String get(String var1, Params var2);

	<T> T get(String var1, TypeReference<T> var2);

	<T> T get(String var1, Params var2, TypeReference<T> var3);

	String post(String var1, Params var2);

	<T> T post(String var1, Params var2, TypeReference<T> var3);

	String put(String var1, Params var2);

	<T> T put(String var1, Params var2, TypeReference<T> var3);

	String delete(String var1);

	<T> T delete(String var1, TypeReference<T> var2);

	String delete(String var1, Params var2);

	<T> T delete(String var1, Params var2, TypeReference<T> var3);

	public static enum EnumHttpConnectParam {
		//Tcp是否粘包(批量封包发送)
		TcpNoDelay(true),
		//总连接池大小
		MaxTotal(500),
		//单个host连接池大小
		DefaultMaxPerRoute(500),
		//连接是否需要验证有效时间
		ValidateAfterInactivity(10000),
		//连接超时时间 【常用】
		ConnectTimeout(10000),
		//socket通讯超时时间 【常用】
		SocketTimeout(15000),
		//请求从连接池获取超时时间
		ConnectionRequestTimeout(2000),
		//连接池共享
		ConnectionManagerShared(true),
		//回收时间间隔 s
		EvictIdleConnectionsTime(60),
		//是否回收
		IsEvictExpiredConnections(true),
		//长连接保持时间 s
		ConnectionTimeToLive(-1),
		//重试次数 【常用】
		RetryCount(3);

		private Object defaultvalue;

		public Object getDefaultValue() {
			return defaultvalue;
		}

		EnumHttpConnectParam(Object defaultvalue) {
			this.defaultvalue = defaultvalue;
		}

		public static EnumHttpConnectParam get(String value) {
			for (EnumHttpConnectParam v : EnumHttpConnectParam.values()) {
				if (v.name().equalsIgnoreCase(value)) {
					return v;
				}
			}
			return null;
		}
	}

	/**
	 * 初始化参数
	 */
	public static class InitMap extends HashMap<EnumHttpConnectParam, Object> {

		@Override
		public String toString() {
			StringBuilder stringBuilder = new StringBuilder();
			for (Entry entry : this.entrySet()) {
				stringBuilder.append(entry.getKey() + ":" + entry.getValue() + ",");
			}
			return StringUtils.trimTrailingCharacter(stringBuilder.toString(), ',');
		}

		public void trySetDefaultParams(EnumHttpConnectParam key, Object defaultValue) {
			if (this.containsKey(key)) {
				return;
			}
			this.put(key, defaultValue);
		}

		public void trySetDefaultParams(String key, Object defaultValue) {
			this.trySetDefaultParams(EnumHttpConnectParam.valueOf(key), defaultValue);
		}

		public <T> T getParams(EnumHttpConnectParam key, Class<T> type) {
			Object value = this.get(key);
			if (value == null) {
				return null;
			}
			return BeanUtil.convert(value, type);
		}

		public <T> T getParams(String key, Class<T> type) {
			return getParams(EnumHttpConnectParam.valueOf(key), type);
		}
	}

	/**
	 * 请求参数
	 */
	class Params {

		private List<Header> headers;
		private Map<String, Object> data;
		private Map<String, Collection<ContentBody>> bodyMultimap;
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
				Iterator var2 = this.data.entrySet().iterator();

				while (var2.hasNext()) {
					Map.Entry<String, Object> entry = (Map.Entry) var2.next();
					tmp.add(new BasicNameValuePair((String) entry.getKey(),
						entry.getValue().toString()));
				}

				return URLEncodedUtils.format(tmp, Charsets.UTF_8);
			}
		}

		public HttpEntity toEntity() {
			if (!this.contentType.equals(ContentType.MULTIPART_FORM_DATA)) {
				return EntityBuilder.create().setContentType(this.contentType)
					.setContentEncoding("utf-8").setText(this.toString()).build();
			} else {
				MultipartEntityBuilder builder = MultipartEntityBuilder.create();
				Iterator var2 = this.data.keySet().iterator();

				while (var2.hasNext()) {
					String key = (String) var2.next();
					Object value = this.data.get(key);

					try {
						builder.addPart(key, new StringBody(value.toString(),
							ContentType.APPLICATION_FORM_URLENCODED));
					} catch (Exception var8) {
						throw new HttpException(var8);
					}
				}

				Map<String, Collection<ContentBody>> items = this.bodyMultimap;
				Iterator var10 = items.keySet().iterator();

				while (var10.hasNext()) {
					String key = (String) var10.next();
					Collection<ContentBody> value = (Collection) items.get(key);
					Iterator var6 = value.iterator();

					while (var6.hasNext()) {
						ContentBody contentBody = (ContentBody) var6.next();
						builder.addPart(key, contentBody);
					}
				}

				return builder.build();
			}
		}

		public static class Builder {

			private Map<String, Object> data = new HashMap<>();
			private Map<String, Collection<ContentBody>> bodyMultimap = new HashMap<>();
			private List<Header> headers = new ArrayList<>();
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
