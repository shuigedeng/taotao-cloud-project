package com.taotao.cloud.log.biz.other.server.application.controller;

import com.alibaba.fastjson.JSON;
import com.taotao.cloud.log.biz.other.server.application.dto.UrlRequest;
import com.taotao.cloud.log.biz.other.server.application.exception.ErrorResponse;
import com.taotao.cloud.log.biz.other.server.application.server.UrlMapServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/shortUrlServer")
@Api("短域名服务相关的api")
@Slf4j
public class ShortUrlController {

	@Autowired
	private UrlMapServer urlMapServer;

	@Value("${shorturl.prefix}")
	private String shortUrlPrefix;


	@ApiOperation(value = "长链接转短链接")
	@ApiImplicitParam(name = "longUrl", value = "域名请求对象", required = true, dataType = "UrlRequest")
	@PostMapping("/getShortUrl")
	public ResponseEntity getShortUrl(@RequestBody UrlRequest request) {
		if (!ValidateLongUrlParas(request)) {
			return ErrorResponse.notFound();
		}
		request.setRequestId(UUID.randomUUID().toString().replaceAll("-", ""));
		log.info("{} request {}", request.getRequestId(), request.getLongUrl());
		return urlMapServer.getShortUrl(request);
	}


	@ApiOperation(value = "短链接获取长链接")
	@ApiImplicitParam(name = "shortUrl", value = "域名请求对象", required = true, dataType = "UrlRequest")
	@GetMapping("/getLongUrl")
	public ResponseEntity getLongUrl(@RequestBody UrlRequest request) {
		if (!ValidateShortUrlParas(request)) {
			return ErrorResponse.notFound();
		}
		request.setRequestId(UUID.randomUUID().toString().replaceAll("-", ""));
		log.info("{} request {}", request.getRequestId(), request.getLongUrl());
		return urlMapServer.getLongUrl(request);
	}

	@PostMapping("/test")
	public ResponseEntity test(@RequestBody JSON json) {
		log.info("request {}", json);
		return ResponseEntity.ok(json);

	}

	private Boolean ValidateLongUrlParas(UrlRequest longUrlReq) {
		if (longUrlReq.getLongUrl() == null || StringUtils.isBlank(longUrlReq.getLongUrl())) {
			log.error("long url is empty");
			return false;
		}
		//要符合固定格式
		if (!longUrlReq.getLongUrl().startsWith("http")) {
			log.error("long url is invalid");
			return false;
		}

		if ((longUrlReq.getLongUrl().length() > 1024)) {
			log.error("url is too long");
			return false;
		}

		return true;
	}

	private Boolean ValidateShortUrlParas(UrlRequest shortUrlReq) {
		if (shortUrlReq.getShortUrl() == null || StringUtils.isBlank(shortUrlReq.getShortUrl())) {
			log.error("short url is empty");
			return false;
		}

		if (!shortUrlReq.getShortUrl().startsWith(shortUrlPrefix)) {
			log.error("short url is invalid");
			return false;
		}
		return true;
	}


}


