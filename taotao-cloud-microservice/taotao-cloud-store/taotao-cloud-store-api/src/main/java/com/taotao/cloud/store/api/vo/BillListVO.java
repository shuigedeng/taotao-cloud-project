package com.taotao.cloud.store.api.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;

/**
 * 结算单VO
 *
 * 
 * @since 2020年3月07日 上午11:04:25
 */
@Schema(description = "结算单VO")
public class BillListVO {

	@Schema(description = "账单ID")
	private String id;

	@Schema(description = "账单号")
	private String sn;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@Schema(description = "结算开始时间")
	private Date startTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@Schema(description = "结算结束时间")
	private Date endTime;

	@JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
	@Schema(description = "出账时间")
	private Date createTime;

	/**
	 * @see BillStatusEnum
	 */
	@Schema(description = "状态：OUT(已出账),RECON(已对账),PASS(已审核),PAY(已付款)")
	private String billStatus;

	@Schema(description = "店铺名称")
	private String storeName;

	@Schema(description = "最终结算金额")
	private Double billPrice;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getBillStatus() {
		return billStatus;
	}

	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}

	public Double getBillPrice() {
		return billPrice;
	}

	public void setBillPrice(Double billPrice) {
		this.billPrice = billPrice;
	}
}
