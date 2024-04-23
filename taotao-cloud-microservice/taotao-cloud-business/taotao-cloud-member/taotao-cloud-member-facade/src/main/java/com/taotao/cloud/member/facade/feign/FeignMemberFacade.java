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

package com.taotao.cloud.member.facade.feign;

import com.taotao.cloud.member.api.feign.IFeignMemberApi;
import com.taotao.cloud.member.api.feign.response.FeignMemberResponse;
import com.taotao.cloud.member.application.service.IMemberService;
import com.taotao.cloud.security.springsecurity.model.SecurityUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,管理员API
 *
 * @since 2020/11/16 10:57
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "内部调用端-会员API", description = "内部调用端-会员API")
public class FeignMemberFacade implements IFeignMemberApi {

	private final IMemberService memberService;

	@Override
	public SecurityUser getMemberSecurityUser(String nicknameOrUserNameOrPhoneOrEmail) {
		return null;
	}

	@Override
	public FeignMemberResponse findMemberById(Long id) {
		return null;
	}

	@Override
	public Boolean updateMemberPoint(Long payPoint, String name, Long memberId, String s) {
		return null;
	}

	@Override
	public FeignMemberResponse findByUsername(String username) {
		return null;
	}

	@Override
	public FeignMemberResponse getById(Long memberId) {
		return null;
	}

	@Override
	public Boolean update(Long memberId, Long storeId) {
		return null;
	}

	@Override
	public Boolean updateById(FeignMemberResponse member) {
		return null;
	}

	@Override
	public List<Map<String, Object>> listFieldsByMemberIds(String s, List<String> ids) {
		return null;
	}
}
