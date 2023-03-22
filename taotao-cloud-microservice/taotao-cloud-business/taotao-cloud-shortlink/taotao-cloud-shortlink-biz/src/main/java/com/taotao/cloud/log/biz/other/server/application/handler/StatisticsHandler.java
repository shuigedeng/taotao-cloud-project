package com.taotao.cloud.log.biz.other.server.application.handler;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shorturl.server.server.application.dto.UrlRequest;
import shorturl.server.server.application.dto.UrlResponse;
import shorturl.server.server.application.threadpool.CommonExecutor;

@Slf4j
@Component
public class StatisticsHandler implements Handler {

	@Autowired
	CommonExecutor commonExecutor;

	@Override
	public void handle(UrlRequest urlReq, UrlResponse urlResponse) {
		//
		commonExecutor.runTask(
			() -> {
				log.info("requestId {}async StaticsHandler", urlReq.getRequestId());
				/*to Statistic */
			}
		);

	}


}
