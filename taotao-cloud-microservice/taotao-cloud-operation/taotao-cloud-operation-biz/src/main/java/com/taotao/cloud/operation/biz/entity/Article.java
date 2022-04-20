package com.taotao.cloud.operation.biz.entity;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.http.HtmlUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.operation.api.enums.ArticleEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.io.Serial;

/**
 * 文章DO
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Article.TABLE_NAME)
@TableName(Article.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Article.TABLE_NAME, comment = "文章表")
public class Article extends BaseSuperEntity<Article, Long> {

	public static final String TABLE_NAME = "li_article";

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "文章标题")
	@NotEmpty(message = "文章标题不能为空")
	@Length(max = 30, message = "文章标题不能超过30个字符")
	private String title;

	@Schema(description = "分类id")
	@NotNull(message = "文章分类不能为空")
	private String categoryId;

	@Schema(description = "文章排序")
	private Integer sort;

	@Schema(description = "文章内容")
	@NotEmpty(message = "文章内容不能为空")
	private String content;

	@Schema(description = "状态")
	private Boolean openStatus;
	/**
	 * @see ArticleEnum
	 */
	@Schema(description = "类型")
	private String type;

	public String getContent() {
		if (CharSequenceUtil.isNotEmpty(content)) {
			return HtmlUtil.unescape(content);
		}
		return content;
	}

}
