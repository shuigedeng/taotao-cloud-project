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
//package com.taotao.cloud.security.exception;
//
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.taotao.cloud.security.serializer.OauthExceptionSerializer;
//import org.springframework.http.HttpStatus;
//import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
//
///**
// * ServerErrorException
// *
// * @author dengtao
// * @version 1.0.0
// * @since 2020/6/15 11:21
// */
//@JsonSerialize(using = OauthExceptionSerializer.class)
//public class ServerErrorException extends OAuth2Exception {
//
//	public ServerErrorException(String msg, Throwable t) {
//		super(msg);
//	}
//
//	@Override
//	public String getOAuth2ErrorCode() {
//		return "server_error";
//	}
//
//	@Override
//	public int getHttpErrorCode() {
//		return HttpStatus.INTERNAL_SERVER_ERROR.value();
//	}
//
//}
