/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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

package com.taotao.cloud.auth.biz.authentication.authentication.oneClick.service;

import com.taotao.cloud.auth.biz.authentication.authentication.oneClick.mobtech.Auth;
import com.taotao.cloud.auth.biz.uaa.enums.ErrorCodeEnum;
import com.taotao.cloud.auth.biz.uaa.exception.Auth2Exception;
import com.taotao.cloud.common.utils.log.LogUtils;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultOneClickLoginService implements OneClickLoginService {

	@Override
	public String callback(String accessToken, Map<String, String> otherParamMap) {

		//todo 需要校验参数
		String opToken = otherParamMap.get("opToken");
		String operator = otherParamMap.get("operator");

		String phoneNumber = null;
		try {
			phoneNumber = Auth.getPhoneNumber(accessToken, opToken, operator);
		} catch (Exception e) {
			throw new Auth2Exception(ErrorCodeEnum.QUERY_MOBILE_FAILURE_OF_ONE_CLICK_LOGIN, accessToken);
		}

		//noinspection ConstantConditions
		if (StrUtil.isEmpty(phoneNumber)) {
			throw new Auth2Exception(ErrorCodeEnum.QUERY_MOBILE_FAILURE_OF_ONE_CLICK_LOGIN, accessToken);
		}

		return phoneNumber;

	}

	@Override
	public void otherParamsHandler(UserDetails userDetails, Map<String, String> otherParamMap) {
		if (MapUtil.isNotEmpty(otherParamMap) && !otherParamMap.isEmpty()) {
			// handler otherParamMap
			LogUtils.info("登录用户: {}", userDetails.getUsername());
			LogUtils.info("登录时的其他请求参数: {}", otherParamMap.toString());
		}
	}
}
