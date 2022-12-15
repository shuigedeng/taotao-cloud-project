package com.taotao.cloud.sys.biz.model.entity.setting;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;


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
@TableName(value = Setting.TABLE_NAME, autoResultMap = true)
@org.hibernate.annotations.Table(appliesTo = Setting.TABLE_NAME, comment = "配置表")
public class Setting extends BaseSuperEntity<Setting, Long> {

	public static final String TABLE_NAME = "tt_setting";

	@Column(name = "name", columnDefinition = "varchar(255) not null comment '名称'")
	private String name;

	@Column(name = "category", columnDefinition = "varchar(255) not null comment '分类'")
	private String category;

	@Column(name = "en_code", unique = true, columnDefinition = "varchar(255) not null comment '编码'")
	private String enCode;

	//@Type(type = "json")
	@TableField(typeHandler = JacksonTypeHandler.class)
	@Column(name = "value", columnDefinition = "json not null comment 'json数据'")
	private Map<String, String> value;

	@Override
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public void setId(Long id) {
		super.setId(id);
	}

	@Builder
	public Setting(Long id, LocalDateTime createTime, Long createBy, LocalDateTime updateTime,
			Long updateBy, Integer version, Boolean delFlag, String name, String category,
			String enCode, Map<String, String> value) {
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
