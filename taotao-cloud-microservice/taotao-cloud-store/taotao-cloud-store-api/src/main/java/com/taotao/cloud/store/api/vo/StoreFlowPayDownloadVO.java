package com.taotao.cloud.store.api.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 店铺流水下载
 *
 * 
 * @date: 2021/8/13 4:14 下午
 */
@Schema(description = "店铺流水下载")
public class StoreFlowPayDownloadVO {

	@CreatedDate
	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@TableField(fill = FieldFill.INSERT)
	@Schema(description = "创建时间", hidden = true)
	private Date createTime;

	@Schema(description = "订单sn")
	private String orderSn;

	@Schema(description = "店铺名称 ")
	private String storeName;

	@Schema(description = "商品名称")
	private String goodsName;

	@Schema(description = "销售量")
	private Integer num;

	@Schema(description = "流水金额")
	private Double finalPrice;

	@Schema(description = "平台收取交易佣金")
	private Double commissionPrice;

	@Schema(description = "平台优惠券 使用金额")
	private Double siteCouponPrice;

	@Schema(description = "单品分销返现支出")
	private Double distributionRebate;

	@Schema(description = "积分活动商品结算价格")
	private Double pointSettlementPrice;

	@Schema(description = "砍价活动商品结算价格")
	private Double kanjiaSettlementPrice;

	@Schema(description = "最终结算金额")
	private Double billPrice;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOrderSn() {
		return orderSn;
	}

	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getFinalPrice() {
		return finalPrice;
	}

	public void setFinalPrice(Double finalPrice) {
		this.finalPrice = finalPrice;
	}

	public Double getCommissionPrice() {
		return commissionPrice;
	}

	public void setCommissionPrice(Double commissionPrice) {
		this.commissionPrice = commissionPrice;
	}

	public Double getSiteCouponPrice() {
		return siteCouponPrice;
	}

	public void setSiteCouponPrice(Double siteCouponPrice) {
		this.siteCouponPrice = siteCouponPrice;
	}

	public Double getDistributionRebate() {
		return distributionRebate;
	}

	public void setDistributionRebate(Double distributionRebate) {
		this.distributionRebate = distributionRebate;
	}

	public Double getPointSettlementPrice() {
		return pointSettlementPrice;
	}

	public void setPointSettlementPrice(Double pointSettlementPrice) {
		this.pointSettlementPrice = pointSettlementPrice;
	}

	public Double getKanjiaSettlementPrice() {
		return kanjiaSettlementPrice;
	}

	public void setKanjiaSettlementPrice(Double kanjiaSettlementPrice) {
		this.kanjiaSettlementPrice = kanjiaSettlementPrice;
	}

	public Double getBillPrice() {
		return billPrice;
	}

	public void setBillPrice(Double billPrice) {
		this.billPrice = billPrice;
	}
}
