package com.taotao.cloud.member.api.dto;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 评价查询条件
 *
 * 
 * @since 2020/11/30 14:52
 */
@Schema(description = "评价查询条件")
//public class EvaluationQueryParams extends PageVO {
public class EvaluationQueryParams {


	@Schema(description = "买家ID")
	private String memberId;

	@Schema(description = "会员名称")
	private String memberName;

	@Schema(description = "卖家名称")
	private String storeName;

	@Schema(description = "卖家ID")
	private String storeId;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "商品ID")
	private String goodsId;

	@Schema(description = "好中差评 , GOOD：好评，MODERATE：中评，WORSE：差评", allowableValues = "GOOD,MODERATE,WORSE")
	private String grade;

	@Schema(description = "是否有图")
	private String haveImage;

	@Schema(description = "评论日期--开始时间")
	private String startTime;

	@Schema(description = "评论日期--结束时间")
	private String endTime;

	@Schema(description = "状态")
	private String status;

	public EvaluationQueryParams() {

	}

	public <T> QueryWrapper<T> queryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(startTime) && StringUtils.isNotEmpty(endTime)) {
			queryWrapper.between("create_time", startTime, endTime);
		}
		if (StringUtils.isNotEmpty(grade)) {
			queryWrapper.eq("grade", grade);
		}
		if (StringUtils.isNotEmpty(goodsName)) {
			queryWrapper.like("goods_name", goodsName);
		}
		if (StringUtils.isNotEmpty(storeName)) {
			queryWrapper.like("store_name", storeName);
		}
		if (StringUtils.isNotEmpty(memberName)) {
			queryWrapper.like("member_name", memberName);
		}
		if (StringUtils.isNotEmpty(goodsId)) {
			queryWrapper.eq("goods_id", goodsId);
		}
		if (StringUtils.isNotEmpty(storeId)) {
			queryWrapper.eq("store_id", storeId);
		}
		if (StringUtils.isNotEmpty(memberId)) {
			queryWrapper.eq("member_id", memberId);
		}
		if (StringUtils.isNotEmpty(haveImage)) {
			queryWrapper.eq("have_image", haveImage);
		}
		if (StringUtils.isNotEmpty(status)) {
			queryWrapper.eq("status", status);
		}
		queryWrapper.eq("delete_flag", false);
		queryWrapper.orderByDesc("create_time");
		return queryWrapper;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getHaveImage() {
		return haveImage;
	}

	public void setHaveImage(String haveImage) {
		this.haveImage = haveImage;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
