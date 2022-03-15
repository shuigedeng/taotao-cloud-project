package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 自定义分词表
 **/
@Entity
@Table(name = CustomWords.TABLE_NAME)
@TableName(CustomWords.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CustomWords.TABLE_NAME, comment = "自定义分词表")
public class CustomWords extends BaseSuperEntity<CustomWords, Long> {

	public static final String TABLE_NAME = "tt_custom_words";

	/**
	 * 分词名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(64) not null comment '分词名称'")
	private String name;

	/**
	 * 是否禁用
	 */
	@Column(name = "disabled", nullable = false, columnDefinition = "int not null default 0 comment '是否禁用'")
	private Integer disabled;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getDisabled() {
		return disabled;
	}

	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
}
