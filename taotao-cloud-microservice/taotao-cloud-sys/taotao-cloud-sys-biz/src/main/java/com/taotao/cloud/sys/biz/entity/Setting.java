package com.taotao.cloud.sys.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 设置
 */

@Entity
@Table(name = Setting.TABLE_NAME)
@TableName(Setting.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Setting.TABLE_NAME, comment = "设置表")
public class Setting extends BaseSuperEntity<Setting, Long> {

	public static final String TABLE_NAME = "tt_sys_setting";

	@Column(name = "setting_value", nullable = false, columnDefinition = "varchar(3660) not null comment '值'")
	private String settingValue;

}
