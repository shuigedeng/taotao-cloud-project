package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 自定义分词表
 **/
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		CustomWords that = (CustomWords) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
