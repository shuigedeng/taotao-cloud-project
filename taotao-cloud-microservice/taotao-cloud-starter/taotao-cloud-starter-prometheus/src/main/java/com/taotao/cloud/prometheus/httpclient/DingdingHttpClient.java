package com.taotao.cloud.prometheus.httpclient;


import com.taotao.cloud.prometheus.model.DingDingNotice;
import com.taotao.cloud.prometheus.model.DingDingResult;

@FunctionalInterface
public interface DingdingHttpClient {

	DingDingResult doSend(DingDingNotice notice);

}
