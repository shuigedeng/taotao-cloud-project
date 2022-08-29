package com.taotao.cloud.sys.biz.model.entity.setting;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.sys.biz.model.entity.system.Dept;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.Objects;


/**
 * 配置表
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-21 21:54:40
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Setting.TABLE_NAME)
@TableName(Setting.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Dept.TABLE_NAME, comment = "配置表")
public class Setting extends BaseSuperEntity<Setting, Long> {

	public static final String TABLE_NAME = "tt_setting";

	@Column(name = "name", columnDefinition = "varchar(255) not null comment '名称'")
	private String name;

	@Column(name = "category", columnDefinition = "varchar(255) not null comment '分类'")
	private String category;

	@Column(name = "en_code", unique = true, columnDefinition = "varchar(255) not null comment '编码'")
	private String enCode;

	@Column(name = "value", columnDefinition = "text not null comment 'json数据'")
	private String value;

	@Override
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public void setId(Long id) {
		super.setId(id);
	}

	@Builder
	public Setting(Long id, LocalDateTime createTime, Long createBy, LocalDateTime updateTime, Long updateBy, Integer version, Boolean delFlag, String name, String category, String enCode, String value) {
		super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
		this.name = name;
		this.category = category;
		this.enCode = enCode;
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Setting setting = (Setting) o;
		return getId() != null && Objects.equals(getId(), setting.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
