
package com.taotao.cloud.sys.application.command.dept.executor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.taotao.cloud.sys.application.command.dept.dto.DeptModifyCmd;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import com.taotao.cloud.sys.domain.dept.entity.DeptEntity;
import com.taotao.cloud.sys.domain.dept.service.DeptDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 修改部门执行器.
 *
 */
@Component
@RequiredArgsConstructor
public class DeptModifyCmdExe {

	private final DeptDomainService deptDomainService;

	/**
	 * 执行修改部门.
	 * @param cmd 修改部门参数
	 */
	//@DS(TENANT)
	public void executeVoid(DeptModifyCmd cmd) {
		deptDomainService.modify(convert(cmd.getDeptCO()));
	}

	private DeptEntity convert(DeptCO deptCO) {
		return null;
//		return DeptEntity.builder()
//			.id(deptCO.getId())
//			.pid(deptCO.getPid())
//			.name(deptCO.getName())
//			.sort(deptCO.getSort())
//			.build();
	}

}
