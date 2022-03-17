package com.taotao.cloud.message.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;


/**
 * 短链接/暂时只用于小程序二维码业务
 */
@Data
@Entity
@Table(name = ShortLink.TABLE_NAME)
@TableName(ShortLink.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = ShortLink.TABLE_NAME, comment = "短链接/暂时只用于小程序二维码业务表")
public class ShortLink extends BaseSuperEntity<ShortLink, Long> {

	public static final String TABLE_NAME = "tt_short_link";

	/**
	 * 原始参数
	 */
	@Column(name = "original_params", nullable = false, columnDefinition = "varchar(255) not null default '' comment '原始参数'")
	private String originalParams;


}
