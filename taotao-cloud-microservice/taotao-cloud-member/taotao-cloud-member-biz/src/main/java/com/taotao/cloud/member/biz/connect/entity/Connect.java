package com.taotao.cloud.member.biz.connect.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.member.biz.connect.entity.enums.ConnectEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = Connect.TABLE_NAME)
@TableName(Connect.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Connect.TABLE_NAME, comment = "联合登陆表")
public class Connect extends BaseSuperEntity<Connect, Long> {

	public static final String TABLE_NAME = "tt_connect";

	/**
	 * 用户id
	 */
	@Column(name = "user_id", columnDefinition = "varchar(32) not null comment '用户id'")
	private String userId;

	/**
	 * 联合登录id
	 */
	@Column(name = "union_id", columnDefinition = "varchar(32) not null comment '联合登录id'")
	private String unionId;

	/**
	 * 联合登录类型
	 *
	 * @see ConnectEnum
	 */
	@Column(name = "union_type", columnDefinition = "varchar(32) not null comment '联合登录类型'")
	private String unionType;


	public Connect(String userId, String unionId, String unionType) {
		this.userId = userId;
		this.unionId = unionId;
		this.unionType = unionType;
	}
}
