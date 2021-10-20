package com.taotao.cloud.uc.api.bo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 部门查询对象
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
public record DeptBO(
	Integer deptId,

	String name,

	Integer parentId,

	Integer sort,

	String remark) implements Serializable {

	static final long serialVersionUID = 1L;


}
