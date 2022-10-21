package com.taotao.cloud.message.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.JpaEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;


/**
 * 短链接/暂时只用于小程序二维码业务
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = ShortLink.TABLE_NAME)
@TableName(ShortLink.TABLE_NAME)
@EntityListeners({JpaEntityListener.class})
@org.hibernate.annotations.Table(appliesTo = ShortLink.TABLE_NAME, comment = "短链接/暂时只用于小程序二维码业务表")
public class ShortLink extends BaseSuperEntity<ShortLink, Long> {

	public static final String TABLE_NAME = "tt_short_link";

	/**
	 * 原始参数
	 */
	@Column(name = "original_params", columnDefinition = "varchar(255) not null default '' comment '原始参数'")
	private String originalParams;


}
