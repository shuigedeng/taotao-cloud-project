package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelCollection;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class CxFeeEO {

	@Excel(name = "单据编号", needMerge = true)
	private String feeNo;
	@Excel(name = "供商卡号", needMerge = true)
	private String supplierCode;
	@Excel(name = "供商名称", needMerge = true)
	private String supplierName;
	@Excel(name = "总金额(元)", needMerge = true)
	private BigDecimal amount;
	@Excel(name = "金额类型", needMerge = true)
	private String amountTypeName;
	@Excel(name = "支付方式编码", needMerge = true)
	private Integer discountWay;
	@Excel(name = "支付方式名称", needMerge = true)
	private String discountWayName;
	@Excel(name = "费用类型", needMerge = true)
	private String rebateType;
	@Excel(name = "费用类型名称", needMerge = true)
	private String rebateDesc;
	@Excel(name = "单据状态", needMerge = true)
	private String feeStatusName;
	@Excel(name = "计划扣费日期", needMerge = true, format = "yyyy-MM-dd")
	private Date collectDate;
	@Excel(name = "采购课组", needMerge = true)
	private String categoryMi;
	@Excel(name = "单据来源", needMerge = true)
	private String source;
	@Excel(name = "创建人", needMerge = true)
	private String userInfo;
	@Excel(name = "备注", needMerge = true)
	private String text;
	@ExcelCollection(name = "单据行项")
	private List<CxFeeItemEO> items;

	@Data
	public static class CxFeeItemEO {

		@Excel(name = "商品/项目编码")
		private String goodsCode;
		@Excel(name = "商品/项目名称")
		private String goodsName;
		@Excel(name = "门店编码")
		private String shopCode;
		@Excel(name = "门店名称")
		private String shopName;
		@Excel(name = "金额(元)")
		private BigDecimal amount;
		@Excel(name = "数量")
		private BigDecimal quantity;
		@Excel(name = "备注")
		private String remark;

		public String getGoodsCode() {
			return goodsCode;
		}

		public void setGoodsCode(String goodsCode) {
			this.goodsCode = goodsCode;
		}

		public String getGoodsName() {
			return goodsName;
		}

		public void setGoodsName(String goodsName) {
			this.goodsName = goodsName;
		}

		public String getShopCode() {
			return shopCode;
		}

		public void setShopCode(String shopCode) {
			this.shopCode = shopCode;
		}

		public String getShopName() {
			return shopName;
		}

		public void setShopName(String shopName) {
			this.shopName = shopName;
		}

		public BigDecimal getAmount() {
			return amount;
		}

		public void setAmount(BigDecimal amount) {
			this.amount = amount;
		}

		public BigDecimal getQuantity() {
			return quantity;
		}

		public void setQuantity(BigDecimal quantity) {
			this.quantity = quantity;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}
	}

	public String getFeeNo() {
		return feeNo;
	}

	public void setFeeNo(String feeNo) {
		this.feeNo = feeNo;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getAmountTypeName() {
		return amountTypeName;
	}

	public void setAmountTypeName(String amountTypeName) {
		this.amountTypeName = amountTypeName;
	}

	public Integer getDiscountWay() {
		return discountWay;
	}

	public void setDiscountWay(Integer discountWay) {
		this.discountWay = discountWay;
	}

	public String getDiscountWayName() {
		return discountWayName;
	}

	public void setDiscountWayName(String discountWayName) {
		this.discountWayName = discountWayName;
	}

	public String getRebateType() {
		return rebateType;
	}

	public void setRebateType(String rebateType) {
		this.rebateType = rebateType;
	}

	public String getRebateDesc() {
		return rebateDesc;
	}

	public void setRebateDesc(String rebateDesc) {
		this.rebateDesc = rebateDesc;
	}

	public String getFeeStatusName() {
		return feeStatusName;
	}

	public void setFeeStatusName(String feeStatusName) {
		this.feeStatusName = feeStatusName;
	}

	public Date getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(Date collectDate) {
		this.collectDate = collectDate;
	}

	public String getCategoryMi() {
		return categoryMi;
	}

	public void setCategoryMi(String categoryMi) {
		this.categoryMi = categoryMi;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<CxFeeItemEO> getItems() {
		return items;
	}

	public void setItems(
		List<CxFeeItemEO> items) {
		this.items = items;
	}
}
