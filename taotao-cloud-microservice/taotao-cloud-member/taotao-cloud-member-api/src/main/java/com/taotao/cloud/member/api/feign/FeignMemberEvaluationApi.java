package com.taotao.cloud.member.api.feign;

import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.member.api.feign.fallback.FeignMemberEvaluationApiFallback;
import com.taotao.cloud.member.api.model.dto.MemberEvaluationDTO;
import com.taotao.cloud.member.api.model.query.EvaluationPageQuery;
import com.taotao.cloud.member.api.model.vo.MemberEvaluationListVO;
import com.taotao.cloud.member.api.model.vo.MemberEvaluationVO;
import com.taotao.cloud.member.api.model.vo.StoreRatingVO;
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
@FeignClient(value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = FeignMemberEvaluationApiFallback.class)
public interface FeignMemberEvaluationApi {


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
	@GetMapping(value = "/member/evaluation")
	Long count(@RequestParam Long goodsId, @RequestParam String name);

	/**
	 * 得到评价数
	 *
	 * @param queryParams 查询参数
	 * @return {@link Result }<{@link Long }>
	 * @since 2022-04-25 16:39:46
	 */
	@GetMapping(value = "/member/evaluationPageQuery")
	Long getEvaluationCount(@RequestParam EvaluationPageQuery queryParams);

	/**
	 * new QueryWrapper<MemberEvaluation>() .between("create_time", DateUtil.yesterday(), new
	 * DateTime())
	 *
	 * @return {@link Result }<{@link List }<{@link Map }<{@link String }, {@link Object }>>>
	 * @since 2022-04-25 16:39:49
	 */
	@GetMapping(value = "/member/memberEvaluationNum")
	List<Map<String, Object>> memberEvaluationNum();

	@GetMapping(value = "/member/memberEvaluationDTO")
	Boolean addMemberEvaluation(@RequestParam MemberEvaluationDTO memberEvaluationDTO,
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
	@GetMapping(value = "/member/evaluation/getStoreRatingVO")
	StoreRatingVO getStoreRatingVO(@RequestParam Long id, @RequestParam String name);

	@GetMapping(value = "/member/evaluation/queryById")
	MemberEvaluationVO queryById(@RequestParam Long id);

	@GetMapping(value = "/member/evaluation/reply")
	boolean reply(@RequestParam Long id, @RequestParam String reply,
		@RequestParam String replyImage);

	@GetMapping(value = "/member/evaluation/queryPage")
	PageResult<MemberEvaluationListVO> queryPage(
		@RequestParam EvaluationPageQuery evaluationPageQuery);
}

