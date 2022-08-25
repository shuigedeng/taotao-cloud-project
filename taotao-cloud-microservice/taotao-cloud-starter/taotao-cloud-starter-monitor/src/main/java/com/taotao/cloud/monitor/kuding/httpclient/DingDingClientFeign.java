package com.taotao.cloud.monitor.kuding.httpclient;

import java.util.Map;


import com.taotao.cloud.monitor.kuding.pojos.dingding.DingDingNotice;
import com.taotao.cloud.monitor.kuding.pojos.dingding.DingDingResult;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface DingDingClientFeign {

	@RequestLine("POST /send?access_token={accessToken}")
	@Headers("Content-Type: application/json; charset=utf-8")
	@Body("{body}")
	DingDingResult post(@Param("accessToken") String accessToken,
						@Param(value = "body", expander = JsonExpander.class) DingDingNotice body,
						@QueryMap Map<String, Object> map);
}
