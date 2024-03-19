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

package com.taotao.cloud.member.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.member.api.feign.fallback.FeignMemberEvaluationApiFallback;
import com.taotao.cloud.member.api.feign.request.FeignEvaluationPageQueryRequest;
import com.taotao.cloud.member.api.feign.request.FeignMemberEvaluationRequest;
import com.taotao.cloud.member.api.feign.response.FeignMemberEvaluationListResponse;
import com.taotao.cloud.member.api.feign.response.FeignMemberEvaluationResponse;
import com.taotao.cloud.member.api.feign.response.FeignStoreRatingResponse;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:37:49
 */
@FeignClient(value = ServiceName.TAOTAO_CLOUD_MEMBER, fallbackFactory = FeignMemberEvaluationApiFallback.class)
public interface IFeignMemberEvaluationApi {

	/**
	 * LambdaQueryWrapper<MemberEvaluation> goodEvaluationQueryWrapper = new LambdaQueryWrapper<>();
	 * goodEvaluationQueryWrapper.eq(MemberEvaluation::getId, goodsId);
	 * goodEvaluationQueryWrapper.eq(MemberEvaluation::getGrade, EvaluationGradeEnum.GOOD.name());
	 *
	 * @param goodsId 商品id
	 * @param name    名字
	 * @return {@link Result }<{@link Long }>
	 * @since 2022-04-25 16:39:41
	 */
	@GetMapping(value = "/member/feign/evaluation")
	Long count(@RequestParam Long goodsId, @RequestParam String name);

	/**
	 * 得到评价数
	 *
	 * @param queryParams 查询参数
	 * @return {@link Result }<{@link Long }>
	 * @since 2022-04-25 16:39:46
	 */
	@GetMapping(value = "/member/feign/evaluationPageQuery")
	Long getEvaluationCount(@RequestParam FeignEvaluationPageQueryRequest queryParams);

	/**
	 * new QueryWrapper<MemberEvaluation>() .between("create_time", DateUtil.yesterday(), new
	 * DateTime())
	 *
	 * @return {@link Result }<{@link List }<{@link Map }<{@link String }, {@link Object }>>>
	 * @since 2022-04-25 16:39:49
	 */
	@GetMapping(value = "/member/feign/memberEvaluationNum")
	List<Map<String, Object>> memberEvaluationNum();

	@GetMapping(value = "/member/feign/memberEvaluationDTO")
	Boolean addMemberEvaluation(@RequestParam FeignMemberEvaluationRequest memberEvaluationDTO,
		@RequestParam boolean b);

	/**
	 * LambdaQueryWrapper<MemberEvaluation> lambdaQueryWrapper = Wrappers.lambdaQuery();
	 * lambdaQueryWrapper.eq(MemberEvaluation::getStoreId, store.getId());
	 * lambdaQueryWrapper.eq(MemberEvaluation::getStatus, SwitchEnum.OPEN.name());
	 *
	 * @param id
	 * @param name
	 * @return
	 */
	@GetMapping(value = "/member/feign/evaluation/getStoreRatingVO")
	FeignStoreRatingResponse getStoreRatingVO(@RequestParam Long id, @RequestParam String name);

	@GetMapping(value = "/member/feign/evaluation/queryById")
	FeignMemberEvaluationResponse queryById(@RequestParam Long id);

	@GetMapping(value = "/member/feign/evaluation/reply")
	boolean reply(@RequestParam Long id, @RequestParam String reply,
		@RequestParam String replyImage);

	@GetMapping(value = "/member/feign/evaluation/queryPage")
	PageResult<FeignMemberEvaluationListResponse> queryPage(
		@RequestParam FeignEvaluationPageQueryRequest evaluationPageQuery);
}
