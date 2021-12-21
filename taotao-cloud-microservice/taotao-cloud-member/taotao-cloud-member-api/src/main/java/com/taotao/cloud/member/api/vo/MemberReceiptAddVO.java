package com.taotao.cloud.member.api.vo;

import com.taotao.cloud.member.api.enums.MemberReceiptEnum;
import io.swagger.v3.oas.annotations.media.Schema;


/**
 * 会员发票添加VO
 *
 * 
 * @since 2021-03-29 14:10:16
 */
@Schema(description = "会员发票")
public class MemberReceiptAddVO {

	private static final long serialVersionUID = -8267092982915677995L;

	@Schema(description = "唯一标识", hidden = true)
	private String id;

	@Schema(description = "发票抬头")
	private String receiptTitle;

	@Schema(description = "纳税人识别号")
	private String taxpayerId;

	@Schema(description = "发票内容")
	private String receiptContent;

	/**
	 * @see MemberReceiptEnum
	 */
	@Schema(description = "发票类型")
	private String receiptType;

	@Schema(description = "是否为默认选项 0：否，1：是")
	private Integer isDefault;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getReceiptTitle() {
		return receiptTitle;
	}

	public void setReceiptTitle(String receiptTitle) {
		this.receiptTitle = receiptTitle;
	}

	public String getTaxpayerId() {
		return taxpayerId;
	}

	public void setTaxpayerId(String taxpayerId) {
		this.taxpayerId = taxpayerId;
	}

	public String getReceiptContent() {
		return receiptContent;
	}

	public void setReceiptContent(String receiptContent) {
		this.receiptContent = receiptContent;
	}

	public String getReceiptType() {
		return receiptType;
	}

	public void setReceiptType(String receiptType) {
		this.receiptType = receiptType;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}
}
