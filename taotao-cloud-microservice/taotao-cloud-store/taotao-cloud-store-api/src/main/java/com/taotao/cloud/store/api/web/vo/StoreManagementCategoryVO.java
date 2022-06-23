package com.taotao.cloud.store.api.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * 店铺经营范围
 *
 * 
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "店铺经营范围")
public class StoreManagementCategoryVO {

	@Schema(description = "已选择")
	private Boolean selected;

	/**
	 * 分类名称
	 */
	private String name;

	/**
	 * 父id, 根节点为0
	 */
	private Long parentId;

	/**
	 * 层级, 从0开始
	 */
	private Integer level;

	/**
	 * 排序值
	 */
	private Integer sortOrder;

	/**
	 * 佣金比例
	 */
	private BigDecimal commissionRate;

	/**
	 * 分类图标
	 */
	private String image;

	/**
	 * 是否支持频道
	 */
	private Boolean supportChannel;

}
