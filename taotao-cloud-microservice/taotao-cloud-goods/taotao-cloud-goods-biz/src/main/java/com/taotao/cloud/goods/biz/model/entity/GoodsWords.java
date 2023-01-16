package com.taotao.cloud.goods.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 商品关键字表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = GoodsWords.TABLE_NAME)
@TableName(GoodsWords.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsWords.TABLE_NAME, comment = "商品关键字表")
public class GoodsWords extends BaseSuperEntity<GoodsWords, Long> {

	public static final String TABLE_NAME = "tt_goods_words";

	/**
	 * 商品关键字
	 */
	@Column(name = "words", columnDefinition = "varchar(255) not null comment '商品关键字'")
	private String words;

	/**
	 * 全拼音
	 */
	@Column(name = "whole_spell", columnDefinition = "varchar(255) not null comment '全拼音'")
	private String wholeSpell;

	/**
	 * 缩写
	 */
	@Column(name = "abbreviate", columnDefinition = "varchar(255) not null comment '缩写'")
	private String abbreviate;

	/**
	 * 类型
	 */
	@Column(name = "type", columnDefinition = "varchar(255) not null comment '类型'")
	private String type;

	/**
	 * 排序
	 */
	@Column(name = "sort", columnDefinition = "int not null default 0  comment '排序'")
	private Integer sort;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		GoodsWords that = (GoodsWords) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
