package com.taotao.cloud.member.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 会员评价VO
 *
 * 
 * @since 2020/11/30 15:00
 */
@Schema(description = "会员评价VO")
public class MemberEvaluationListVO {

	@Schema(description = "评论ID")
	private String id;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "好中差评", allowableValues = "GOOD,NEUTRAL,BAD")
	private String grade;

	@Schema(description = "评价内容")
	private String content;

	@Schema(description = "状态 ", allowableValues = " OPEN 正常 ,CLOSE 关闭")
	private String status;

	@Schema(description = "回复状态")
	private Boolean replyStatus;

	@Schema(description = "创建时间")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	@Schema(description = "物流评分")
	private Integer deliveryScore;

	@Schema(description = "服务评分")
	private Integer serviceScore;

	@Schema(description = "描述评分")
	private Integer descriptionScore;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getReplyStatus() {
		return replyStatus;
	}

	public void setReplyStatus(Boolean replyStatus) {
		this.replyStatus = replyStatus;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getDeliveryScore() {
		return deliveryScore;
	}

	public void setDeliveryScore(Integer deliveryScore) {
		this.deliveryScore = deliveryScore;
	}

	public Integer getServiceScore() {
		return serviceScore;
	}

	public void setServiceScore(Integer serviceScore) {
		this.serviceScore = serviceScore;
	}

	public Integer getDescriptionScore() {
		return descriptionScore;
	}

	public void setDescriptionScore(Integer descriptionScore) {
		this.descriptionScore = descriptionScore;
	}
}
