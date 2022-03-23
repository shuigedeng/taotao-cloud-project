package com.taotao.cloud.sys.api.bo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

/**
 * 部门查询对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-23 08:49:43
 */
public record DeptBO(
	Integer deptId,

	String name,

	Integer parentId,

	Integer sort,

	String remark) implements Serializable {

	static final long serialVersionUID = 1L;


}
