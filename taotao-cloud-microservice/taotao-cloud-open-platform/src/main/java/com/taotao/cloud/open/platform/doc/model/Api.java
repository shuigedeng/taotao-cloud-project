package com.taotao.cloud.open.platform.doc.model;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * API接口信息
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:11:15
 */
@Data
public class Api {

	/**
	 * 开放api名称
	 */
	private String openApiName;

	/**
	 * 接口名
	 */
	private String name;

	/**
	 * 接口中文名
	 */
	private String cnName;

	/**
	 * 接口完整名
	 */
	private String fullName;

	/**
	 * 接口描述
	 */
	private String describe;

	/**
	 * 接口里的方法
	 */
	private List<Method> methods = new ArrayList<>();
}
