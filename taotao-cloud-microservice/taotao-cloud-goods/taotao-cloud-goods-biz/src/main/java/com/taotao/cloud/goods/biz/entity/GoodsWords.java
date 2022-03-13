package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 商品关键字表
 */
@Entity
@Table(name = GoodsWords.TABLE_NAME)
@TableName(GoodsWords.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsWords.TABLE_NAME, comment = "商品关键字表")
public class GoodsWords extends BaseSuperEntity<GoodsWords, Long> {

	public static final String TABLE_NAME = "tt_goods_words";


	/**
	 * 商品关键字
	 */
	@Column(name = "words", nullable = false, columnDefinition = "varchar(64) not null comment '商品关键字'")
	private String words;

	/**
	 * 全拼音
	 */
	@Column(name = "whole_spell", nullable = false, columnDefinition = "varchar(64) not null comment '全拼音'")
	private String wholeSpell;

	/**
	 * 缩写
	 */
	@Column(name = "abbreviate", nullable = false, columnDefinition = "varchar(64) not null comment '缩写'")
	private String abbreviate;

	/**
	 * 类型
	 */
	@Column(name = "type", nullable = false, columnDefinition = "varchar(64) not null comment '类型'")
	private String type;

	/**
	 * 排序
	 */
	@Column(name = "sort", nullable = false, columnDefinition = "int not null comment '排序'")
	private Integer sort;

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public String getWholeSpell() {
		return wholeSpell;
	}

	public void setWholeSpell(String wholeSpell) {
		this.wholeSpell = wholeSpell;
	}

	public String getAbbreviate() {
		return abbreviate;
	}

	public void setAbbreviate(String abbreviate) {
		this.abbreviate = abbreviate;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
