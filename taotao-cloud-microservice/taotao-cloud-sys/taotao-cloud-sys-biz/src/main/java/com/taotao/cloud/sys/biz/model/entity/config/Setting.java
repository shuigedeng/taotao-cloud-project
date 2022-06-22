package com.taotao.cloud.sys.biz.model.entity.config;

import com.baomidou.mybatisplus.annotation.TableName;
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


/**
 * 配置表
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-21 21:54:40
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = Setting.TABLE_NAME)
@TableName(Setting.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Setting.TABLE_NAME, comment = "设置表")
public class Setting extends BaseSuperEntity<Setting, Long> {

	public static final String TABLE_NAME = "tt_setting";

	@Column(name = "setting_value", columnDefinition = "varchar(3660) not null comment '值'")
	private String settingValue;

	@Override
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public void setId(Long id) {
		super.setId(id);
	}
}
