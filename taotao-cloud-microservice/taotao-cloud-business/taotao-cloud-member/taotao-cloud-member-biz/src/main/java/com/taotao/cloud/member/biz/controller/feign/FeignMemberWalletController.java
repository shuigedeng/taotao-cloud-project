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

package com.taotao.cloud.member.biz.controller.feign;

import com.taotao.cloud.member.api.feign.IFeignMemberWalletApi;
import com.taotao.cloud.member.api.feign.request.FeignMemberWalletUpdateRequest;
import com.taotao.cloud.member.biz.service.business.IMemberService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "内部调用端-会员钱包API", description = "内部调用端-会员钱包API")
public class FeignMemberWalletController implements IFeignMemberWalletApi {

	private final IMemberService memberService;

	@Override
	public boolean increase(FeignMemberWalletUpdateRequest memberWalletUpdateDTO) {
		return false;
	}

	@Override
	public boolean save(Long id, String username) {
		return false;
	}
}
