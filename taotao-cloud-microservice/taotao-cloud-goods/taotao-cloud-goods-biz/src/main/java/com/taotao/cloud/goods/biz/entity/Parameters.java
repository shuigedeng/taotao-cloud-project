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
	@Column(name = "param_name", nullable = false, columnDefinition = "varchar(64) not null comment '参数名称'")
	private String paramName;

	/**
	 * 选择值
	 */
	@Column(name = "options", nullable = false, columnDefinition = "varchar(64) not null comment '选择值'")
	private String options;

	/**
	 * 是否可索引，0 不显示 1 显示
	 */
	@Column(name = "is_index", nullable = false, columnDefinition = "int not null comment '是否可索引，0 不显示 1 显示'")
	private Integer isIndex;

	/**
	 * 是否必填 是1否0
	 */
	@Column(name = "required", nullable = false, columnDefinition = "int not null comment '是否必填 是1否0'")
	private Integer required;

	/**
	 * 参数分组id
	 */
	@Column(name = "group_id", nullable = false, columnDefinition = "varchar(64) not null comment '参数分组id'")
	private String groupId;

	/**
	 * 分类id
	 */
	@Column(name = "category_id", nullable = false, columnDefinition = "varchar(64) not null comment '分类id'")
	private String categoryId;

	/**
	 * 排序
	 */
	@Column(name = "sort", nullable = false, columnDefinition = "int not null comment '排序'")
	private Integer sort;
}
