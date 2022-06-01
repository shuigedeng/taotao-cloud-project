package com.taotao.cloud.member.api.feign;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.ServiceName;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.member.api.dto.MemberEvaluationDTO;
import com.taotao.cloud.member.api.feign.fallback.FeignMemberServiceFallback;
import com.taotao.cloud.member.api.query.EvaluationPageQuery;
import com.taotao.cloud.member.api.vo.MemberEvaluationListVO;
import com.taotao.cloud.member.api.vo.MemberEvaluationVO;
import com.taotao.cloud.member.api.vo.StoreRatingVO;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;
import java.util.Map;

/**
 * 远程调用会员用户模块
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-25 16:37:49
 */
@FeignClient(contextId = "IFeignMemberEvaluationService", value = ServiceName.TAOTAO_CLOUD_MEMBER_CENTER, fallbackFactory = FeignMemberServiceFallback.class)
public interface IFeignMemberEvaluationService {


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
	Result<Long> count(Long goodsId, String name);

	/**
	 * 得到评价数
	 *
	 * @param queryParams 查询参数
	 * @return {@link Result }<{@link Long }>
	 * @since 2022-04-25 16:39:46
	 */
	Result<Long> getEvaluationCount(EvaluationPageQuery queryParams);

	/**
	 * new QueryWrapper<MemberEvaluation>()
	 * .between("create_time", DateUtil.yesterday(), new DateTime())
	 *
	 * @return {@link Result }<{@link List }<{@link Map }<{@link String }, {@link Object }>>>
	 * @since 2022-04-25 16:39:49
	 */
	Result<List<Map<String, Object>>> memberEvaluationNum();

	Result<Boolean> addMemberEvaluation(MemberEvaluationDTO memberEvaluationDTO, boolean b);

	/**
	 * LambdaQueryWrapper<MemberEvaluation> lambdaQueryWrapper = Wrappers.lambdaQuery();
	 * 			lambdaQueryWrapper.eq(MemberEvaluation::getStoreId, store.getId());
	 * 			lambdaQueryWrapper.eq(MemberEvaluation::getStatus, SwitchEnum.OPEN.name());
	 * @param id
	 * @param name
	 * @return
	 */
	StoreRatingVO getStoreRatingVO(Long id, String name);

	MemberEvaluationVO queryById(Long id);

	void reply(Long id, String reply, String replyImage);

	IPage<MemberEvaluationListVO> queryPage(EvaluationPageQuery evaluationPageQuery);
}

