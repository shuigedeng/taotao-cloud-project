package com.taotao.cloud.prometheus.httpclient;

import com.taotao.cloud.prometheus.pojos.dingding.DingDingNotice;
import com.taotao.cloud.prometheus.pojos.dingding.DingDingResult;

@FunctionalInterface
public interface DingdingHttpClient {

	DingDingResult doSend(DingDingNotice notice);

}
