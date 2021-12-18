package com.taotao.cloud.sys.biz.entity.sms;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 短信签名
 *
 * @author Chopper
 * @since 2021/1/30 4:13 下午
 */
@Entity
@Table(name = SmsSign.TABLE_NAME)
@TableName(SmsSign.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SmsSign.TABLE_NAME, comment = "短信签名表")
public class SmsSign extends BaseSuperEntity<SmsSign, Long> {

	public static final String TABLE_NAME = "tt_sys_sms_sign";

	@Column(name = "sign_name", nullable = false, columnDefinition = "varchar(2000) not null comment '签名名称'")
	private String signName;

	@Column(name = "sign_source", nullable = false, columnDefinition = "varchar(2000) not null comment '签名来源'")
	private Integer signSource;

	@Column(name = "remark", nullable = false, columnDefinition = "varchar(2000) not null comment '短信签名申请说明'")
	private String remark;

	@Column(name = "business_license", nullable = false, columnDefinition = "varchar(2000) not null comment '营业执照'")
	private String businessLicense;

	@Column(name = "license", nullable = false, columnDefinition = "varchar(2000) not null comment '授权委托书'")
	private String license;

	@Column(name = "sign_status", nullable = false, columnDefinition =
		"int not null default 0 comment '签名审核状态  0：审核中。"
			+ "     * 1：审核通过。"
			+ "     * 2：审核失败，请在返回参数Reason中查看审核失败原因。'")
	private Integer signStatus;

	@Column(name = "reason", nullable = false, columnDefinition = "varchar(2000) not null comment '审核备注'")
	private String reason;

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public Integer getSignSource() {
		return signSource;
	}

	public void setSignSource(Integer signSource) {
		this.signSource = signSource;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBusinessLicense() {
		return businessLicense;
	}

	public void setBusinessLicense(String businessLicense) {
		this.businessLicense = businessLicense;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	public Integer getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(Integer signStatus) {
		this.signStatus = signStatus;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
