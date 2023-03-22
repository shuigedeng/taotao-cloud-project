package com.taotao.cloud.log.biz.other.server.application.server;

import com.taotao.cloud.log.biz.other.server.application.dto.UrlRequest;
import org.springframework.http.ResponseEntity;

public interface UrlMapServer {

	public ResponseEntity getLongUrl(UrlRequest shortUrlReq);

	public ResponseEntity getShortUrl(UrlRequest longUrlReq);
}
