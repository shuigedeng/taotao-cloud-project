package com.taotao.cloud.web.xss;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.web.util.XssUtil;

/**
 * 基于xss的 json 序列化器 在本项目中，没有使用该类
 */
public class XssStringJsonSerializer extends JsonSerializer<String> {

	@Override
	public Class<String> handledType() {
		return String.class;
	}

	@Override
	public void serialize(String value, JsonGenerator jsonGenerator,
		SerializerProvider serializerProvider) {
		if (StrUtil.isEmpty(value)) {
			return;
		}
		try {
			String encodedValue = XssUtil.xssClean(value, null);
			jsonGenerator.writeString(encodedValue);
		} catch (Exception e) {
			LogUtil.error("序列化失败:[{0}]", value, e);
		}
	}

}
