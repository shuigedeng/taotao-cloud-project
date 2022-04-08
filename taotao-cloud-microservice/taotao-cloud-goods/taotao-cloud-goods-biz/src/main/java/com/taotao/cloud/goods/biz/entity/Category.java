package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品分类表
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Category.TABLE_NAME)
@TableName(Category.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Category.TABLE_NAME, comment = "商品分类表")
public class Category extends BaseSuperEntity<Category, Long> {

	public static final String TABLE_NAME = "tt_category";

	/**
	 * 分类名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(64) not null comment '分类名称'")
	private String name;

	/**
	 * 父id, 根节点为0
	 */
	@Column(name = "parent_id", nullable = false, columnDefinition = "varchar(64) not null comment '父id, 根节点为0'")
	private Long parentId;

	/**
	 * 层级, 从0开始
	 */
	@Column(name = "level", nullable = false, columnDefinition = "int not null default 0 comment '层级, 从0开始'")
	private Integer level;

	/**
	 * 排序值
	 */
	@Column(name = "sort_order", nullable = false, columnDefinition = "int not null default 0 comment '排序值'")
	private Integer sortOrder;

	/**
	 * 佣金比例
	 */
	@Column(name = "commission_rate", nullable = false, columnDefinition = "decimal(10,2) not null comment '佣金比例'")
	private BigDecimal commissionRate;

	/**
	 * 分类图标
	 */
	@Column(name = "image", nullable = false, columnDefinition = "varchar(64) not null comment '分类图标'")
	private String image;

	/**
	 * 是否支持频道
	 */
	@Column(name = "support_channel", nullable = false, columnDefinition = "boolean not null comment '是否支持频道'")
	private Boolean supportChannel;

	public Category(Long id, String createBy, LocalDateTime createTime, String updateBy, LocalDateTime updateTime, Boolean deleteFlag, String name, String parentId, Integer level, BigDecimal sortOrder, BigDecimal commissionRate, String image, Boolean supportChannel) {
		super(id, createBy, createTime, updateBy, updateTime, deleteFlag);
		this.name = name;
		this.parentId = parentId;
		this.level = level;
		this.sortOrder = sortOrder;
		this.commissionRate = commissionRate;
		this.image = image;
		this.supportChannel = supportChannel;
	}
	//
	//public Category(String id, String name, String parentId, Integer level, BigDecimal sortOrder, BigDecimal commissionRate, String image, Boolean supportChannel) {
	//    this.name = name;
	//    this.parentId = parentId;
	//    this.level = level;
	//    this.sortOrder = sortOrder;
	//    this.commissionRate = commissionRate;
	//    this.image = image;
	//    this.supportChannel = supportChannel;
	//}
}
