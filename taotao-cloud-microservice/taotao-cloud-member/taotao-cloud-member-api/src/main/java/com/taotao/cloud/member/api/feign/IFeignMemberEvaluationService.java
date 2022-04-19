package com.taotao.cloud.member.api.feign;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.member.api.dto.EvaluationQueryParams;
import com.taotao.cloud.member.api.feign.fallback.RemoteMemberFallbackImpl;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;
import java.util.Map;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@FeignClient(contextId = "IFeignMemberEvaluationService", value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = RemoteMemberFallbackImpl.class)
public interface IFeignMemberEvaluationService {


	/**
	 * LambdaQueryWrapper<MemberEvaluation> goodEvaluationQueryWrapper = new LambdaQueryWrapper<>();
	 * 		goodEvaluationQueryWrapper.eq(MemberEvaluation::getId, goodsId);
	 * 		goodEvaluationQueryWrapper.eq(MemberEvaluation::getGrade, EvaluationGradeEnum.GOOD.name());
	 */
	Result<Long> count(Long goodsId, String name);


	Result<Long> getEvaluationCount(EvaluationQueryParams queryParams);

	/**
	 * new QueryWrapper<MemberEvaluation>()
	 * 				.between("create_time", DateUtil.yesterday(), new DateTime())
	 */
	Result<List<Map<String, Object>>> memberEvaluationNum();
}

