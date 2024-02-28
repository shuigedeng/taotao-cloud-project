

package com.taotao.cloud.sys.application.command.dept;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DeptConvertor;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.dto.dept.DeptInsertCmd;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.admin.gatewayimpl.database.DeptMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * 新增部门执行器.
 *
 *
 */
@Component
@RequiredArgsConstructor
public class DeptInsertCmdExe {

	private final DeptGateway deptGateway;

	private final DeptMapper deptMapper;

	private final DeptConvertor deptConvertor;

	/**
	 * 执行新增部门.
	 * @param cmd 新增部门参数
	 * @return 执行新增结果
	 */
	@DS(TENANT)
	public Result<Boolean> execute(DeptInsertCmd cmd) {
		DeptCO co = cmd.getDeptCO();
		long count = deptMapper.selectCount(Wrappers.lambdaQuery(DeptDO.class).eq(DeptDO::getName, co.getName()));
		if (count > 0) {
			throw new SystemException("部门已存在，请重新填写");
		}
		return Result.of(deptGateway.insert(deptConvertor.toEntity(co)));
	}

}
