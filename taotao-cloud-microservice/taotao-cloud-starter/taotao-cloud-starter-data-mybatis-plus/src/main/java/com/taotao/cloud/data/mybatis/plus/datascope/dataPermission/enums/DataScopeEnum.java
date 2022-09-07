package com.taotao.cloud.data.mybatis.plus.datascope.dataPermission.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据范围枚举类
 *
 * 用于实现数据级别的权限
 *
 * @author fxz
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

	/**
	 * 全部数据权限
	 */
	ALL(1),

	/**
	 * 指定部门数据权限
	 */
	DEPT_CUSTOM(2),

	/**
	 * 部门数据权限
	 */
	DEPT_ONLY(3),

	/**
	 * 部门及以下数据权限
	 */
	DEPT_AND_CHILD(4),

	/**
	 * 仅本人数据权限
	 */
	SELF(5);

	/**
	 * 范围
	 */
	private final Integer scope;

}
