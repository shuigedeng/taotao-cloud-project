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
 * 自定义分词表
 **/
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = CustomWords.TABLE_NAME)
@TableName(CustomWords.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CustomWords.TABLE_NAME, comment = "自定义分词表")
public class CustomWords extends BaseSuperEntity<CustomWords, Long> {

	public static final String TABLE_NAME = "tt_custom_words";

	/**
	 * 分词名称
	 */
	@Column(name = "name", columnDefinition = "varchar(255) not null comment '分词名称'")
	private String name;

	/**
	 * 是否禁用
	 */
	@Column(name = "disabled", columnDefinition = "int null default 0 comment '是否禁用'")
	private Integer disabled;
}
