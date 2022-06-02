package com.taotao.cloud.operation.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.operation.api.enums.ArticleCategoryEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 文章分类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-21 16:59:38
 */
@Getter
@Setter
@ToString(callSuper = true)
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = ArticleCategory.TABLE_NAME)
@TableName(ArticleCategory.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = ArticleCategory.TABLE_NAME, comment = "文章分类表")
public class ArticleCategory extends BaseSuperEntity<ArticleCategory, Long> {

	public static final String TABLE_NAME = "tt_article_category";
	/**
	 * 分类名称
	 */
	@Column(name = "article_category_name", columnDefinition = "varchar(255) not null comment '分类名称 '")
	private String articleCategoryName;
	/**
	 * 父分类ID
	 */
	@Column(name = "parent_id", columnDefinition = "varchar(255) not null comment '父分类ID '")
	private String parentId;
	/**
	 * 排序
	 */
	@Column(name = "sort_num", columnDefinition = "int not null comment '排序 '")
	private Integer sortNum;
	/**
	 * 层级 层级最小为0 层级最大为3
	 */
	@Column(name = "level", columnDefinition = "int not null comment '层级 层级最小为0 层级最大为3'")
	private Integer level;

	/**
	 * 业务类型
	 *
	 * @see ArticleCategoryEnum
	 */
	@Column(name = "biz_type", columnDefinition = "varchar(255) not null comment '业务类型 '")
	private String type;

	public Integer getSort() {
		if (sortNum == null) {
			return 0;
		}
		return sortNum;
	}

	public Integer getLevel() {
		if (level == null) {
			return 1;
		}
		return level;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		ArticleCategory that = (ArticleCategory) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
