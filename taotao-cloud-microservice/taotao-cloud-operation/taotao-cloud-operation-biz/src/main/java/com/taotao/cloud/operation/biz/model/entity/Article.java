package com.taotao.cloud.operation.biz.model.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.operation.api.enums.ArticleEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 文章DO
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
@Table(name = Article.TABLE_NAME)
@TableName(Article.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Article.TABLE_NAME, comment = "文章表")
public class Article extends BaseSuperEntity<Article, Long> {

	public static final String TABLE_NAME = "tt_article";

	@Serial
	private static final long serialVersionUID = 1L;
	/**
	 * 文章标题
	 */
	@Column(name = "title", columnDefinition = "varchar(255) not null comment '文章标题 '")
	private String title;
	/**
	 * 分类id
	 */
	@Column(name = "category_id", columnDefinition = "varchar(255) not null comment '分类id '")
	private String categoryId;
	/**
	 * 文章排序
	 */
	@Column(name = "sort_num", columnDefinition = "int not null comment '文章排序 '")
	private Integer sortNum;
	/**
	 * 文章内容
	 */
	@Column(name = "content", columnDefinition = "varchar(255) not null comment '文章内容 '")
	private String content;
	/**
	 * 状态
	 */
	@Column(name = "open_status", columnDefinition = "boolean not null comment '状态 '")
	private Boolean openStatus;
	/**
	 * 业务类型
	 *
	 * @see ArticleEnum
	 */
	@Column(name = "type", columnDefinition = "varchar(255) not null comment '业务类型 '")
	private String type;

	public String getContent() {
		if (CharSequenceUtil.isNotEmpty(content)) {
			return HtmlUtil.unescape(content);
		}
		return content;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Article article = (Article) o;
		return getId() != null && Objects.equals(getId(), article.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
