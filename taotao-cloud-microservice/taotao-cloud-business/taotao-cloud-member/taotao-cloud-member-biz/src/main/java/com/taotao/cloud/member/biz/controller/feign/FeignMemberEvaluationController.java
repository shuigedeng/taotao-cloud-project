package com.taotao.cloud.member.biz.controller.feign;

import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.member.api.feign.IFeignMemberEvaluationApi;
import com.taotao.cloud.member.api.model.dto.MemberEvaluationDTO;
import com.taotao.cloud.member.api.model.page.EvaluationPageQuery;
import com.taotao.cloud.member.api.model.vo.MemberEvaluationListVO;
import com.taotao.cloud.member.api.model.vo.MemberEvaluationVO;
import com.taotao.cloud.member.api.model.vo.StoreRatingVO;
import com.taotao.cloud.member.biz.service.business.IMemberService;
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
public class FeignMemberEvaluationController implements IFeignMemberEvaluationApi {

	private final IMemberService memberService;


	@Override
	public Long count(Long goodsId, String name) {
		return null;
	}

	@Override
	public Long getEvaluationCount(EvaluationPageQuery queryParams) {
		return null;
	}

	@Override
	public List<Map<String, Object>> memberEvaluationNum() {
		return null;
	}

	@Override
	public Boolean addMemberEvaluation(MemberEvaluationDTO memberEvaluationDTO, boolean b) {
		return null;
	}

	@Override
	public StoreRatingVO getStoreRatingVO(Long id, String name) {
		return null;
	}

	@Override
	public MemberEvaluationVO queryById(Long id) {
		return null;
	}

	@Override
	public boolean reply(Long id, String reply, String replyImage) {
		return false;
	}

	@Override
	public PageResult<MemberEvaluationListVO> queryPage(EvaluationPageQuery evaluationPageQuery) {
		return null;
	}
}
