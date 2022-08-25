package com.taotao.cloud.monitor.kuding.httpclient;


import com.taotao.cloud.monitor.kuding.pojos.dingding.DingDingNotice;
import com.taotao.cloud.monitor.kuding.pojos.dingding.DingDingResult;

@FunctionalInterface
public interface DingdingHttpClient {

	DingDingResult doSend(DingDingNotice notice);

}
