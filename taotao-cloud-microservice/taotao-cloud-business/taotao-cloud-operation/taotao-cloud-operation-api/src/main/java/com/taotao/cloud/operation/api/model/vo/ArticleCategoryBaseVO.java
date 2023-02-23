package com.taotao.cloud.operation.api.model.vo;

import com.taotao.cloud.operation.api.enums.ArticleCategoryEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 文章分类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ArticleCategoryBaseVO {

	private Long id;

	/**
	 * 分类名称
	 */
	private String articleCategoryName;
	/**
	 * 父分类ID
	 */
	private String parentId;
	/**
	 * 排序
	 */
	private Integer sortNum;
	/**
	 * 层级 层级最小为0 层级最大为3
	 */
	private Integer level;

	/**
	 * 业务类型
	 *
	 * @see ArticleCategoryEnum
	 */
	private String type;

}
