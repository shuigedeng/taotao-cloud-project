package com.taotao.cloud.order.biz.model.entity.purchase;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * 报价单
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:02:30
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

	public static final String TABLE_NAME = "tt_purchase_quoted";

	/**
	 * 采购单ID
	 */
	@Column(name = "purchase_order_id", columnDefinition = "bigint not null comment '采购单ID'")
    private Long purchaseOrderId;
	/**
	 * 标题
	 */
	@Column(name = "title", columnDefinition = "varchar(255) not null comment '标题'")
    private String title;
	/**
	 * 报价说明
	 */
	@Column(name = "context", columnDefinition = "varchar(255) not null comment '报价说明'")
    private String context;
	/**
	 * 附件
	 */
	@Column(name = "annex", columnDefinition = "varchar(255) not null comment '附件'")
    private String annex;
	/**
	 * 公司名称
	 */
	@Column(name = "company_name", columnDefinition = "varchar(255) not null comment '公司名称'")
    private String companyName;
	/**
	 * 联系人
	 */
	@Column(name = "contacts", columnDefinition = "varchar(255) not null comment '联系人'")
    private String contacts;
	/**
	 * 联系电话
	 */
	@Column(name = "contact_number", columnDefinition = "varchar(255) not null comment '联系电话'")
    private String contactNumber;
	/**
	 * 报价人
	 */
	@Column(name = "member_id", columnDefinition = "varchar(255) not null comment '报价人'")
    private String memberId;

	@Override
	public boolean equals(Object o) {
				if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		PurchaseQuoted purchaseQuoted = (PurchaseQuoted) o;
		return getId() != null && Objects.equals(getId(), purchaseQuoted.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
