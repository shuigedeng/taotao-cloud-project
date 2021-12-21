package com.taotao.cloud.member.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

/**
 * 会员评价VO
 *
 * 
 * @since 2020/11/30 15:00
 */
@Schema(description = "会员评价VO")
//public class MemberEvaluationVO extends MemberEvaluation {
public class MemberEvaluationVO {

	private static final long serialVersionUID = 6696978796248845481L;

	@Schema(description = "评论图片")
	private List<String> evaluationImages;

	@Schema(description = "回复评论图片")
	private List<String> replyEvaluationImages;

	//public MemberEvaluationVO(MemberEvaluation memberEvaluation) {
	//    BeanUtil.copyProperties(memberEvaluation, this);
	//}


	public List<String> getEvaluationImages() {
		return evaluationImages;
	}

	public void setEvaluationImages(List<String> evaluationImages) {
		this.evaluationImages = evaluationImages;
	}

	public List<String> getReplyEvaluationImages() {
		return replyEvaluationImages;
	}

	public void setReplyEvaluationImages(List<String> replyEvaluationImages) {
		this.replyEvaluationImages = replyEvaluationImages;
	}
}
