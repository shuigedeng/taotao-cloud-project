package com.taotao.cloud.sys.biz.entity.sms;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 短信模板
 *
 * @since 2021/1/30 4:13 下午
 */
@Entity
@Table(name = SmsTemplate.TABLE_NAME)
@TableName(SmsTemplate.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SmsTemplate.TABLE_NAME, comment = "短信模板表")
public class SmsTemplate extends BaseSuperEntity<SmsTemplate, Long> {

	public static final String TABLE_NAME = "tt_sys_sms_template";

	@Column(name = "template_name", nullable = false, columnDefinition = "varchar(2000) not null comment '模板名称'")
	private String templateName;

	@Column(name = "template_type", nullable = false, columnDefinition = "varchar(2000) not null comment '短信类型'")
	private Integer templateType;

	@Column(name = "remark", nullable = false, columnDefinition = "varchar(2000) not null comment '短信模板申请说明'")
	private String remark;

	@Column(name = "template_content", nullable = false, columnDefinition = "varchar(2000) not null comment '模板内容'")
	private String templateContent;

	@Column(name = "template_status", nullable = false, columnDefinition =
		"int not null default 0 comment '模板审核状态  0：审核中。"
			+ "     * 1：审核通过。"
			+ "     * 2：审核失败，请在返回参数Reason中查看审核失败原因。'")
	private Integer templateStatus;

	@Column(name = "template_code", nullable = false, columnDefinition = "varchar(2000) not null comment '短信模板CODE'")
	private String templateCode;

	@Column(name = "reason", nullable = false, columnDefinition = "varchar(2000) not null comment '审核备注'")
	private String reason;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public Integer getTemplateType() {
		return templateType;
	}

	public void setTemplateType(Integer templateType) {
		this.templateType = templateType;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public Integer getTemplateStatus() {
		return templateStatus;
	}

	public void setTemplateStatus(Integer templateStatus) {
		this.templateStatus = templateStatus;
	}

	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
