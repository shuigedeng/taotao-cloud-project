///*
// * Copyright 2002-2021 the original author or authors.
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      https://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.taotao.cloud.security.component;
//
//import cn.hutool.core.util.StrUtil;
//import com.fasterxml.jackson.core.JsonGenerator;
//import com.fasterxml.jackson.databind.SerializerProvider;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.fasterxml.jackson.databind.ser.std.StdSerializer;
//import com.taotao.cloud.common.constant.CommonConstant;
//import com.taotao.cloud.common.utils.IdGeneratorUtil;
//import java.io.IOException;
//import java.time.LocalDateTime;
//import org.slf4j.MDC;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.InternalAuthenticationServiceException;
//import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
//import org.springframework.security.oauth2.common.exceptions.InvalidScopeException;
//import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
//import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException;
//import org.springframework.security.oauth2.common.exceptions.UnsupportedResponseTypeException;
//import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
//
//
///**
// * 添加CustomWebResponseExceptionTranslator，登录发生异常时指定exceptionTranslator
// *
// * @author shuigedeng
// * @version 1.0.0
// * @since 2020/4/30 09:09
// */
//public class WebResponseExceptionTranslatorComponent extends DefaultWebResponseExceptionTranslator {
//
//	public static final String BAD_MSG = "坏的凭证";
//
//	@Override
//	public ResponseEntity<OAuth2Exception> translate(Exception e) throws Exception {
//		OAuth2Exception oAuth2Exception;
//		if (e.getMessage() != null && e.getMessage().equals(BAD_MSG)) {
//			oAuth2Exception = new InvalidGrantException("用户名或密码错误", e);
//		} else if (e instanceof InternalAuthenticationServiceException) {
//			oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
//		} else if (e instanceof RedirectMismatchException) {
//			oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
//		} else if (e instanceof InvalidScopeException) {
//			oAuth2Exception = new InvalidGrantException(e.getMessage(), e);
//		} else {
//			oAuth2Exception = new UnsupportedResponseTypeException("登录失败:未查询到用户", e);
//		}
//		ResponseEntity<OAuth2Exception> response = super.translate(oAuth2Exception);
//		OAuth2Exception auth2Exception = response.getBody();
//
//		assert auth2Exception != null;
//		BootOAuth2Exception exception = new BootOAuth2Exception(auth2Exception.getMessage(),
//			auth2Exception);
//
//		return new ResponseEntity<>(exception, response.getHeaders(), HttpStatus.OK);
//	}
//
//	@JsonSerialize(using = BootOAuthExceptionJacksonSerializer.class)
//	public static class BootOAuth2Exception extends OAuth2Exception {
//
//		public BootOAuth2Exception(String msg, Throwable t) {
//			super(msg, t);
//		}
//
//		public BootOAuth2Exception(String msg) {
//			super(msg);
//		}
//	}
//
//	public static class BootOAuthExceptionJacksonSerializer extends
//		StdSerializer<BootOAuth2Exception> {
//
//		protected BootOAuthExceptionJacksonSerializer() {
//			super(BootOAuth2Exception.class);
//		}
//
//		@Override
//		public void serialize(BootOAuth2Exception value, JsonGenerator json,
//			SerializerProvider serializerProvider) throws IOException {
//			json.writeStartObject();
//			json.writeObjectField("code", value.getHttpErrorCode());
//			json.writeObjectField("message", value.getMessage());
//			json.writeObjectField("data", null);
//			json.writeObjectField("type", CommonConstant.ERROR);
//			json.writeObjectField("requestId",
//				StrUtil.isNotBlank(MDC.get(CommonConstant.TRACE_ID)) ? MDC
//					.get(CommonConstant.TRACE_ID) : IdGeneratorUtil.getIdStr());
//			json.writeObjectField("timestamp",
//				CommonConstant.DATETIME_FORMATTER.format(LocalDateTime.now()));
//			json.writeEndObject();
//		}
//	}
//}
