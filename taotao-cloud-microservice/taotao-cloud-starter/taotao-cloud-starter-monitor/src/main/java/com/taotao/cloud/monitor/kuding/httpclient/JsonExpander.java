package com.taotao.cloud.monitor.kuding.httpclient;

import com.google.gson.Gson;

import feign.Param.Expander;

public class JsonExpander implements Expander {

	private static final Gson gson = new Gson();

	@Override
	public String expand(Object value) {
		return gson.toJson(value);
	}

}
