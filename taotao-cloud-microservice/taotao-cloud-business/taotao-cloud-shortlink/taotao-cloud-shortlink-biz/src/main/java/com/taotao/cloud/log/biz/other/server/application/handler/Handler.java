package com.taotao.cloud.log.biz.other.server.application.handler;

import shorturl.server.server.application.dto.UrlRequest;
import shorturl.server.server.application.dto.UrlResponse;

public interface Handler {

	public void handle(UrlRequest longUrlReq, UrlResponse urlResponse);
}
