
package com.taotao.cloud.sys.application.command.dept.executor.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.taotao.boot.common.model.Result;
import com.taotao.cloud.sys.application.command.dept.dto.DeptGetQry;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import com.taotao.cloud.sys.infrastructure.persistent.dept.mapper.DeptMapper;
import com.taotao.cloud.sys.infrastructure.persistent.dept.po.DeptPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 查看部门执行器.
 *
 */
@Component
@RequiredArgsConstructor
public class DeptGetQryExe {

	private final DeptMapper deptMapper;

	/**
	 * 执行查看部门.
	 * @param qry 查看部门参数
	 * @return 部门
	 */
	//@DS(TENANT)
	public DeptCO execute(DeptGetQry qry) {
		return convert(deptMapper.selectById(qry.getId()));
	}

	private DeptCO convert(DeptPO deptDO) {
//		return DeptCO.builder()
//			.path(deptDO.getPath())
//			.sort(deptDO.getSort())
//			.id(deptDO.getId())
//			.pid(deptDO.getPid())
//			.name(deptDO.getName())
//			.build();
		return null;
	}

}
