package com.taotao.cloud.sys.biz.model.bo;

import io.soabase.recordbuilder.core.RecordBuilder;

import java.io.Serializable;

/**
 * 部门查询对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-23 08:49:43
 */
@RecordBuilder
public record DeptBO(
	Integer deptId,

	String name,

	Integer parentId,

	Integer sort,

	String remark) implements Serializable {

	static final long serialVersionUID = 1L;


}
