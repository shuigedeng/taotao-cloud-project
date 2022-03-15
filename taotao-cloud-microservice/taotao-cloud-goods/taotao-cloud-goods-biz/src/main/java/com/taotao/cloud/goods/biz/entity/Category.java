package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品分类表
 */
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
	private String parentId;

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

	//public Category(String id, String createBy, Date createTime, String updateBy, Date updateTime, Boolean deleteFlag, String name, String parentId, Integer level, BigDecimal sortOrder, Double commissionRate, String image, Boolean supportChannel) {
	//    super(id, createBy, createTime, updateBy, updateTime, deleteFlag);
	//    this.name = name;
	//    this.parentId = parentId;
	//    this.level = level;
	//    this.sortOrder = sortOrder;
	//    this.commissionRate = commissionRate;
	//    this.image = image;
	//    this.supportChannel = supportChannel;
	//}
	//
	//public Category(String id, String name, String parentId, Integer level, BigDecimal sortOrder, Double commissionRate, String image, Boolean supportChannel) {
	//    this.name = name;
	//    this.parentId = parentId;
	//    this.level = level;
	//    this.sortOrder = sortOrder;
	//    this.commissionRate = commissionRate;
	//    this.image = image;
	//    this.supportChannel = supportChannel;
	//}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public BigDecimal getCommissionRate() {
		return commissionRate;
	}

	public void setCommissionRate(BigDecimal commissionRate) {
		this.commissionRate = commissionRate;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getSupportChannel() {
		return supportChannel;
	}

	public void setSupportChannel(Boolean supportChannel) {
		this.supportChannel = supportChannel;
	}
}
