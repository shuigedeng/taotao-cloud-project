/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sms.service.impl;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sms.properties.AliSmsProperties;
import com.taotao.cloud.sms.service.SmsService;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Random;
import org.springframework.lang.NonNull;

/**
 * AliSmsServiceImpl
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-07 20:44:10
 */
public class AliSmsServiceImpl implements SmsService {

	private final AliSmsProperties aliSmsProperties;
	private final IAcsClient iAcsClient;

	public AliSmsServiceImpl(AliSmsProperties aliSmsProperties, IAcsClient iAcsClient) {
		this.aliSmsProperties = aliSmsProperties;
		this.iAcsClient = iAcsClient;
	}

	@Override
	public String sendSms(@NonNull String phoneNumber, @NonNull String signName,
		@NonNull String templateCode, @NonNull String templateParam) {
		CommonRequest request = request();
		request.putQueryParameter("PhoneNumbers", phoneNumber);
		request.putQueryParameter("SignName", signName);
		request.putQueryParameter("TemplateCode", templateCode);
		request.putQueryParameter("TemplateParam", templateParam);

		try {
			CommonResponse response = iAcsClient.getCommonResponse(request);
			LogUtil.info(response.getData());
			return response.getData();
		} catch (Exception e) {
			LogUtil.error("发送短信异常：{}", e);
		}
		return null;
	}

	@Override
	public String sendRandCode(int digits) {
		StringBuilder sBuilder = new StringBuilder();
		Random rd = new Random(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());
		for (int i = 0; i < digits; ++i) {
			sBuilder.append(rd.nextInt(9));
		}
		return sBuilder.toString();
	}

	/**
	 * request
	 *
	 * @return {@link com.aliyuncs.CommonRequest }
	 * @author shuigedeng
	 * @since 2021-09-07 20:44:27
	 */
	private CommonRequest request() {
		CommonRequest request = new CommonRequest();
		request.setSysMethod(MethodType.POST);
		request.setSysDomain(aliSmsProperties.getDomain());
		request.setSysVersion(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		request.setSysAction("SendSms");
		request.putQueryParameter("RegionId", aliSmsProperties.getRegionId());
		return request;
	}
}
