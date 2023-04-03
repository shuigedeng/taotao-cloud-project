package com.taotao.cloud.log.biz.log.core.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.log.biz.log.core.db.convert.LogConvert;
import com.taotao.cloud.log.biz.log.dto.LoginLogDto;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 登录日志
 *
 * @author shuigedeng
 * @date 2021/8/12
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = LoginLogDb.TABLE_NAME)
@TableName(LoginLogDb.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = LoginLogDb.TABLE_NAME, comment = "app配置表")
public class LoginLogDb extends BaseSuperEntity<LoginLogDb, Long> {

	public static final String TABLE_NAME = "tt_login_log";

	/**
	 * 用户账号id
	 */
	private Long userId;

	/**
	 * 用户名称
	 */
	private String account;

	/**
	 * 登录成功状态
	 */
	private Boolean login;

	/**
	 * 登录终端
	 */
	private String client;

	/**
	 * 登录方式
	 */
	private String loginType;

	/**
	 * 登录IP地址
	 */
	private String ip;

	/**
	 * 登录地点
	 */
	private String loginLocation;

	/**
	 * 浏览器类型
	 */
	private String browser;

	/**
	 * 操作系统
	 */
	private String os;

	/**
	 * 提示消息
	 */
	private String msg;

	/**
	 * 访问时间
	 */
	private LocalDateTime loginTime;

	public LoginLogDto toDto() {
		return LogConvert.CONVERT.convert(this);
	}
}
