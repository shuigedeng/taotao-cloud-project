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

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.member.api.feign.IFeignMemberEvaluationApi;
import com.taotao.cloud.member.api.feign.request.FeignEvaluationPageQueryRequest;
import com.taotao.cloud.member.api.feign.request.FeignMemberEvaluationRequest;
import com.taotao.cloud.member.api.feign.response.FeignMemberEvaluationListResponse;
import com.taotao.cloud.member.api.feign.response.FeignMemberEvaluationResponse;
import com.taotao.cloud.member.api.feign.response.FeignStoreRatingResponse;
import com.taotao.cloud.member.application.service.IMemberService;
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
@Tag(name = "内部调用端-管理员API", description = "内部调用端-管理员API")
public class FeignMemberEvaluationFacade implements IFeignMemberEvaluationApi {

    private final IMemberService memberService;

	@Override
	public Long count(Long goodsId, String name) {
		return null;
	}

	@Override
	public Long getEvaluationCount(FeignEvaluationPageQueryRequest queryParams) {
		return null;
	}

	@Override
	public List<Map<String, Object>> memberEvaluationNum() {
		return null;
	}

	@Override
	public Boolean addMemberEvaluation(FeignMemberEvaluationRequest memberEvaluationDTO,
		boolean b) {
		return null;
	}

	@Override
	public FeignStoreRatingResponse getStoreRatingVO(Long id, String name) {
		return null;
	}

	@Override
	public FeignMemberEvaluationResponse queryById(Long id) {
		return null;
	}

	@Override
	public boolean reply(Long id, String reply, String replyImage) {
		return false;
	}

	@Override
	public PageResult<FeignMemberEvaluationListResponse> queryPage(
		FeignEvaluationPageQueryRequest evaluationPageQuery) {
		return null;
	}
}
