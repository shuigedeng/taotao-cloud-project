package com.taotao.cloud.log.biz.entity;

import com.taotao.cloud.data.jpa.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

/**
 * 会员登录日志表
 *
 * @author dengtao
 * @date 2020/11/13 09:46
 * @since v1.0
 */
@Data
@SuperBuilder
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tt_member_login")
@org.hibernate.annotations.Table(appliesTo = "tt_member_login", comment = "会员登录日志表")
public class MemberLogin extends BaseEntity {

	private static final long serialVersionUID = 6887296988458221221L;

	/**
	 * 会员id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "bigint not null comment '会员id'")
	private Long memberId;

	/**
	 * 用户登录时间
	 */
	@Column(name = "login_time", nullable = false, columnDefinition = "timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户登录时间'")
	private LocalDateTime loginTime;

	/**
	 * 登陆ip
	 */
	@Column(name = "login_ip", columnDefinition = "varchar(12) not null COMMENT '登录IP'")
	private String loginIp;

	/**
	 * 登录状态
	 *
	 * @see LoginStatusEnum
	 */
	@Column(name = "login_status", nullable = false, columnDefinition = "int not null default 0 comment '登录状态 0-未成功 1-成功'")
	@Builder.Default
	private Integer loginStatus = 0;

}
