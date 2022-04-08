package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 商品参数表
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Parameters.TABLE_NAME)
@TableName(Parameters.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Parameters.TABLE_NAME, comment = "商品参数表")
public class Parameters extends BaseSuperEntity<Parameters, Long> {

	public static final String TABLE_NAME = "tt_parameters";

	/**
	 * 参数名称
	 */
	@Column(name = "param_name", columnDefinition = "varchar(255) not null comment '参数名称'")
	private String paramName;

	/**
	 * 选择值
	 */
	@Column(name = "options", columnDefinition = "varchar(255) not null comment '选择值'")
	private String options;

	/**
	 * 是否可索引，0 不显示 1 显示
	 */
	@Column(name = "is_index", columnDefinition = "int not null default 1 comment '是否可索引，0 不显示 1 显示'")
	private Integer isIndex;

	/**
	 * 是否必填 是1否0
	 */
	@Column(name = "required", columnDefinition = "int not null comment '是否必填 是1否0'")
	private Integer required;

	/**
	 * 参数分组id
	 */
	@Column(name = "group_id", columnDefinition = "bigint not null comment '参数分组id'")
	private Long groupId;

	/**
	 * 分类id
	 */
	@Column(name = "category_id", columnDefinition = "bigint not null comment '分类id'")
	private Long categoryId;

	/**
	 * 排序
	 */
	@Column(name = "sort", columnDefinition = "int not null comment '排序'")
	private Integer sort;
}
