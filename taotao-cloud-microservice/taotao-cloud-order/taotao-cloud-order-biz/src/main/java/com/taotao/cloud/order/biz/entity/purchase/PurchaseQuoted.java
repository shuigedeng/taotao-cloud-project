package com.taotao.cloud.order.biz.entity.purchase;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.taotao.cloud.order.biz.entity.trade.OrderLog;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/**
 * 报价单
 *
 * 
 * @since 2020/11/26 20:43
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = PurchaseQuoted.TABLE_NAME)
@TableName(PurchaseQuoted.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = PurchaseQuoted.TABLE_NAME, comment = "供求单报价表")
public class PurchaseQuoted extends BaseSuperEntity<PurchaseQuoted, Long> {

	public static final String TABLE_NAME = "li_purchase_quoted";

	/**
	 * 采购单ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '采购单ID'")
    private String purchaseOrderId;
	/**
	 * 标题
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '标题'")
    private String title;
	/**
	 * 报价说明
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '报价说明'")
    private String context;
	/**
	 * 附件
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '附件'")
    private String annex;
	/**
	 * 公司名称
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '公司名称'")
    private String companyName;
	/**
	 * 联系人
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '联系人'")
    private String contacts;
	/**
	 * 联系电话
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '联系电话'")
    private String contactNumber;
	/**
	 * 报价人
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '报价人'")
    private String memberId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		PurchaseQuoted purchaseQuoted = (PurchaseQuoted) o;
		return getId() != null && Objects.equals(getId(), purchaseQuoted.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
