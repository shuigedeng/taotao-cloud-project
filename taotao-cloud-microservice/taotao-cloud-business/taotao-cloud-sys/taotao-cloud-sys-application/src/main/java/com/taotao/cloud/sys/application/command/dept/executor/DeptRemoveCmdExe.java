
package com.taotao.cloud.sys.application.command.dept.executor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.taotao.cloud.sys.application.command.dept.dto.DeptRemoveCmd;
import com.taotao.cloud.sys.domain.dept.service.DeptDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 删除部门执行器.
 */
@Component
@RequiredArgsConstructor
public class DeptRemoveCmdExe {

	private final DeptDomainService deptDomainService;

	/**
	 * 执行删除部门.
	 * @param cmd 删除部门参数
	 */
	//@DS(TENANT)
	public void executeVoid(DeptRemoveCmd cmd) {
		deptDomainService.remove(cmd.getIds());
	}

}
