
package com.taotao.cloud.sys.application.command.dept.executor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.taotao.cloud.sys.application.command.dept.dto.DeptCreateCmd;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import com.taotao.cloud.sys.domain.dept.entity.DeptEntity;
import com.taotao.cloud.sys.domain.dept.service.DeptDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 新增部门执行器.
 */
@Component
@RequiredArgsConstructor
public class DeptCreateCmdExe {

	private final DeptDomainService deptDomainService;

	/**
	 * 执行新增部门.
	 * @param cmd 新增部门参数
	 */
//	@DS(TENANT)
	public void executeVoid(DeptCreateCmd cmd) {
		deptDomainService.create(convert(cmd.getDeptCO()));
	}

	private DeptEntity convert(DeptCO deptCO) {
		return null;
//		return DeptEntity.builder()
//			.id(IdGenerator.defaultSnowflakeId())
//			.pid(deptCO.getPid())
//			.name(deptCO.getName())
//			.sort(deptCO.getSort())
//			.build();
	}

}
