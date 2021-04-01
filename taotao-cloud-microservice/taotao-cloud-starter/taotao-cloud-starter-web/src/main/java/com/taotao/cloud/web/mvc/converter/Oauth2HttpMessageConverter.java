package com.taotao.cloud.web.mvc.converter;///*
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
//package com.taotao.cloud.core.mvc.converter;
//
//import org.springframework.http.HttpInputMessage;
//import org.springframework.http.HttpOutputMessage;
//import org.springframework.http.MediaType;
//import org.springframework.http.converter.AbstractHttpMessageConverter;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.http.converter.HttpMessageNotWritableException;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
//
//import java.io.IOException;
//
///**
// * Oauth2HttpMessageConverter
// *
// * @author dengtao
// * @version 1.0.0
// * @since 2020/10/20 11:01
// */
//public class Oauth2HttpMessageConverter extends
//	AbstractHttpMessageConverter<DefaultOAuth2AccessToken> {
//
//	private final MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
//
//	public Oauth2HttpMessageConverter() {
//		super(MediaType.APPLICATION_JSON_UTF8);
//	}
//
//	@Override
//	protected boolean supports(Class<?> clazz) {
//		return clazz.equals(DefaultOAuth2AccessToken.class);
//	}
//
//	@Override
//	protected DefaultOAuth2AccessToken readInternal(Class<? extends DefaultOAuth2AccessToken> clazz,
//		HttpInputMessage inputMessage)
//		throws IOException, HttpMessageNotReadableException {
//		throw new UnsupportedOperationException(
//			"This converter is only used for converting DefaultOAuth2AccessToken to json.");
//	}
//
//	@Override
//	protected void writeInternal(DefaultOAuth2AccessToken accessToken,
//		HttpOutputMessage outputMessage) throws IOException,
//		HttpMessageNotWritableException {
//		mappingJackson2HttpMessageConverter
//			.write(accessToken, MediaType.APPLICATION_JSON_UTF8, outputMessage);
//	}
//}
//
