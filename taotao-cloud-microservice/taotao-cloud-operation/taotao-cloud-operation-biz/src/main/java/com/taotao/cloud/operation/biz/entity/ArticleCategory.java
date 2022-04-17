package com.taotao.cloud.operation.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.operation.api.enums.ArticleCategoryEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 文章分类
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ArticleCategory.TABLE_NAME)
@TableName(ArticleCategory.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = ArticleCategory.TABLE_NAME, comment = "文章分类表")
public class ArticleCategory extends BaseSuperEntity<ArticleCategory, Long> {

	public static final String TABLE_NAME = "li_article_category";

	@Schema(description = "分类名称")
	@NotEmpty(message = "分类名称不能为空")
	private String articleCategoryName;

	@Schema(description = "父分类ID")
	private String parentId;

	@Schema(description = "排序")
	@Min(value = 0, message = "排序值最小0，最大9999999999")
	@Max(value = 999999999, message = "排序值最小0，最大9999999999")
	@NotNull(message = "排序值不能为空")
	private Integer sort;

	@Schema(description = "层级")
	@Min(value = 0, message = "层级最小为0")
	@Max(value = 3, message = "层级最大为3")
	private Integer level;

	/**
	 * @see ArticleCategoryEnum
	 */
	@Schema(description = "类型")
	private String type;

	public Integer getSort() {
		if (sort == null) {
			return 0;
		}
		return sort;
	}

	public Integer getLevel() {
		if (level == null) {
			return 1;
		}
		return level;
	}
}
