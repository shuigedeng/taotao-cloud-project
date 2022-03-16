package com.taotao.cloud.common.utils.common;


import com.taotao.cloud.common.utils.collection.MapUtil;
import com.taotao.cloud.common.utils.lang.StringUtil;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 构造URL
 */
public class UrlBuilder {

    private final Map<String, String> params = new LinkedHashMap<>(7);
    private String baseUrl;

    private UrlBuilder() {

    }

    /**
     * @param baseUrl 基础路径
     * @return the new {@code UrlBuilder}
     */
    public static UrlBuilder fromBaseUrl(String baseUrl) {
        UrlBuilder builder = new UrlBuilder();
        builder.setBaseUrl(baseUrl);
        return builder;
    }

    /**
     * 只读的参数Map
     *
     * @return unmodifiable Map
     * @since 1.15.0
     */
    public Map<String, Object> getReadOnlyParams() {
        return Collections.unmodifiableMap(params);
    }

    /**
     * 添加参数
     *
     * @param key   参数名称
     * @param value 参数值
     * @return this UrlBuilder
     */
    public UrlBuilder queryParam(String key, Object value) {
        if (StringUtil.isEmpty(key)) {
            throw new RuntimeException("参数名不能为空");
        }
        String valueAsString = (value != null ? value.toString() : null);
        this.params.put(key, valueAsString);

        return this;
    }

    /**
     * 添加参数
     *
     * @param value 参数值
     * @return this UrlBuilder
     */
    public UrlBuilder pathAppend(String value) {
        if (StringUtil.isEmpty(value)) {
            throw new RuntimeException("参数不能为空");
        }
        this.setBaseUrl(this.baseUrl += value);
        return this;
    }

    /**
     * 构造url
     *
     * @return url
     */
    public String build() {
        return this.build(false);
    }

    /**
     * 构造url
     *
     * @param encode 转码
     * @return url
     */
    public String build(boolean encode) {
        if (MapUtil.isEmpty(this.params)) {
            return this.baseUrl;
        }
        String baseUrl = StringUtil.appendIfNotContain(this.baseUrl, "?", "&");
        String paramString = MapUtil.parseMapToString(this.params, encode);
        return baseUrl + paramString;
    }

	public Map<String, String> getParams() {
		return params;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}
}
