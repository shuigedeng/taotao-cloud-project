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

package com.taotao.cloud.member.api.feign.fallback;

import com.taotao.boot.common.model.result.PageResult;
import com.taotao.cloud.member.api.feign.MemberEvaluationApi;
import com.taotao.cloud.member.api.feign.request.EvaluationPageQueryApiRequest;
import com.taotao.cloud.member.api.feign.request.MemberEvaluationApiRequest;
import com.taotao.cloud.member.api.feign.response.MemberEvaluationListApiResponse;
import com.taotao.cloud.member.api.feign.response.MemberEvaluationApiResponse;
import com.taotao.cloud.member.api.feign.response.StoreRatingApiResponse;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2020/11/20 下午4:10
 */
public class MemberEvaluationApiFallback implements
	FallbackFactory<MemberEvaluationApi> {

	@Override
	public MemberEvaluationApi create(Throwable throwable) {
		return new MemberEvaluationApi() {

			@Override
			public Long count(Long goodsId, String name) {
				return null;
			}

			@Override
			public Long getEvaluationCount(EvaluationPageQueryApiRequest queryParams) {
				return null;
			}

			@Override
			public List<Map<String, Object>> memberEvaluationNum() {
				return null;
			}

			@Override
			public Boolean addMemberEvaluation(MemberEvaluationApiRequest memberEvaluationDTO,
				boolean b) {
				return null;
			}

			@Override
			public StoreRatingApiResponse getStoreRatingVO(Long id, String name) {
				return null;
			}

			@Override
			public MemberEvaluationApiResponse queryById(Long id) {
				return null;
			}

			@Override
			public boolean reply(Long id, String reply, String replyImage) {
				return false;
			}

			@Override
			public PageResult<MemberEvaluationListApiResponse> queryPage(
				EvaluationPageQueryApiRequest evaluationPageQuery) {
				return null;
			}
		};
	}
}
