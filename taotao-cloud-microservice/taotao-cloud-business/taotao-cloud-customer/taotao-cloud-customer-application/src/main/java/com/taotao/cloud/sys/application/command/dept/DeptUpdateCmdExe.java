

package com.taotao.cloud.sys.application.command.dept;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DeptConvertor;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.dto.dept.DeptUpdateCmd;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.admin.gatewayimpl.database.DeptMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DeptDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.laokou.common.i18n.utils.ObjectUtil;
import org.laokou.common.i18n.utils.ValidatorUtil;
import org.springframework.stereotype.Component;

import static org.laokou.common.i18n.common.ValCodes.SYSTEM_ID_REQUIRE;
import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * 修改部门执行器.
 *
 *
 */
@Component
@RequiredArgsConstructor
public class DeptUpdateCmdExe {

	private final DeptGateway deptGateway;

	private final DeptMapper deptMapper;

	private final DeptConvertor deptConvertor;

	/**
	 * 执行修改部门.
	 * @param cmd 修改部门参数
	 * @return 执行修改结果
	 */
	@DS(TENANT)
	public Result<Boolean> execute(DeptUpdateCmd cmd) {
		DeptCO co = cmd.getDeptCO();
		Long id = co.getId();
		if (ObjectUtil.isNull(id)) {
			throw new SystemException(ValidatorUtil.getMessage(SYSTEM_ID_REQUIRE));
		}
		long count = deptMapper
			.selectCount(Wrappers.lambdaQuery(DeptDO.class).eq(DeptDO::getName, co.getName()).ne(DeptDO::getId, id));
		if (count > 0) {
			throw new SystemException("部门已存在，请重新填写");
		}
		if (co.getId().equals(co.getPid())) {
			throw new SystemException("上级部门不能为当前部门");
		}
		return Result.of(deptGateway.update(deptConvertor.toEntity(co)));
	}

}
