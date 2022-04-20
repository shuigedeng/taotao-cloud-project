package com.taotao.cloud.order.biz.entity.order;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.order.biz.entity.aftersale.AfterSale;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.*;


/**
 * 发票表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Receipt.TABLE_NAME)
@TableName(Receipt.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Receipt.TABLE_NAME, comment = "发票表")
public class Receipt extends BaseSuperEntity<Receipt, Long> {

	public static final String TABLE_NAME = "tt_receipt";

    @Serial
    private static final long serialVersionUID = -8210927482915675995L;

	/**
	 * 订单编号
	 */
    @Column(name = "member_id", columnDefinition = "varchar(64) not null comment '订单编号'")
    private String orderSn;
	/**
	 * 发票抬头
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '发票抬头'")
    private String receiptTitle;
	/**
	 * 纳税人识别号
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '纳税人识别号'")
    private String taxpayerId;
	/**
	 * 发票内容
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '发票内容'")
    private String receiptContent;
	/**
	 * 发票金额
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '发票金额'")
    private BigDecimal receiptPrice;
	/**
	 * 会员ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员ID'")
    private String memberId;
	/**
	 * 会员名称
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '会员名称'")
    private String memberName;
	/**
	 * 商家ID
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '商家ID'")
    private String storeId;
	/**
	 * 商家名称
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '商家名称'")
    private String storeName;
	/**
	 * 发票状态 0未开 1已开
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '发票状态 0未开 1已开'")
    private Integer receiptStatus;
	/**
	 * 发票详情
	 */
	@Column(name = "member_id", columnDefinition = "varchar(64) not null comment '发票详情'")
    private String receiptDetail;

}
