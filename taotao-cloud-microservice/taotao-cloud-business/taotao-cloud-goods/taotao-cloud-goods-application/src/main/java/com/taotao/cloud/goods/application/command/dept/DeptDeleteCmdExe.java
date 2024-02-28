

package com.taotao.cloud.goods.application.command.dept;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.dto.dept.DeptDeleteCmd;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 删除部门执行器.
 *
 *
 */
@Component
@RequiredArgsConstructor
public class DeptDeleteCmdExe {

	private final DeptGateway deptGateway;

	/**
	 * 执行删除部门.
	 * @param cmd 删除部门参数
	 * @return 执行删除结果
	 */
	@DS(TENANT)
	public Result<Boolean> execute(DeptDeleteCmd cmd) {
		return Result.of(deptGateway.deleteById(cmd.getId()));
	}

}
