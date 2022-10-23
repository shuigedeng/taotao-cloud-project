package com.taotao.cloud.data.mybatisplus.datascope.perm.local;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.taotao.cloud.common.model.SecurityUser;
import com.taotao.cloud.data.mybatisplus.datascope.perm.NestedPermission;
import com.taotao.cloud.data.mybatisplus.datascope.perm.Permission;

import java.util.Optional;

/**
 * 忽略数据上下文
 */
public class DataPermContextHolder {
	private static final ThreadLocal<Permission> PERMISSION_LOCAL = new TransmittableThreadLocal<>();
	private static final ThreadLocal<NestedPermission> NESTED_PERMISSION_LOCAL = new TransmittableThreadLocal<>();
	private static final ThreadLocal<SecurityUser> USER_DETAIL_LOCAL = new TransmittableThreadLocal<>();

	/**
	 * 设置 数据权限控制注解
	 */
	public static void putPermission(Permission permission) {
		PERMISSION_LOCAL.set(permission);
	}

	/**
	 * 获取 数据权限控制注解
	 */
	public static Permission getPermission() {
		return PERMISSION_LOCAL.get();
	}

	/**
	 * 设置 数据权限控制注解
	 */
	public static void putNestedPermission(NestedPermission nestedPermission) {
		NESTED_PERMISSION_LOCAL.set(nestedPermission);
	}

	/**
	 * 获取 数据权限控制注解
	 */
	public static NestedPermission getNestedPermission() {
		return NESTED_PERMISSION_LOCAL.get();
	}

	/**
	 * 设置 用户缓存
	 */
	public static void putUserDetail(SecurityUser dataPerm) {
		USER_DETAIL_LOCAL.set(dataPerm);
	}

	/**
	 * 获取 用户缓存
	 */
	public static Optional<SecurityUser> getUserDetail() {
		return Optional.ofNullable(USER_DETAIL_LOCAL.get());
	}

	/**
	 * 清除线程变量(数据权限控制和用户信息)
	 */
	public static void clearUserAndPermission() {
		USER_DETAIL_LOCAL.remove();
		PERMISSION_LOCAL.remove();
	}

	/**
	 * 清除线程变量(嵌套权限注解)
	 */
	public static void clearNestedPermission() {
		NESTED_PERMISSION_LOCAL.remove();
	}
}
