package com.taotao.cloud.member.api.feign.fallback;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.member.api.dto.MemberEvaluationDTO;
import com.taotao.cloud.member.api.feign.IFeignMemberEvaluationService;
import com.taotao.cloud.member.api.feign.IFeignMemberService;
import com.taotao.cloud.member.api.query.EvaluationPageQuery;
import com.taotao.cloud.member.api.vo.MemberVO;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;
import java.util.Map;

/**
 * RemoteMemberFallbackImpl
 *
 * @author shuigedeng
 * @since 2020/11/20 下午4:10
 * @version 2022.03
 */
public class FeignMemberEvaluationServiceFallback implements FallbackFactory<IFeignMemberEvaluationService> {
	@Override
	public IFeignMemberEvaluationService create(Throwable throwable) {
		return new IFeignMemberEvaluationService() {

			@Override
			public Result<Long> count(Long goodsId, String name) {
				return null;
			}

			@Override
			public Result<Long> getEvaluationCount(EvaluationPageQuery queryParams) {
				return null;
			}

			@Override
			public Result<List<Map<String, Object>>> memberEvaluationNum() {
				return null;
			}

			@Override
			public void addMemberEvaluation(MemberEvaluationDTO memberEvaluationDTO, boolean b) {

			}
		};
	}
}
