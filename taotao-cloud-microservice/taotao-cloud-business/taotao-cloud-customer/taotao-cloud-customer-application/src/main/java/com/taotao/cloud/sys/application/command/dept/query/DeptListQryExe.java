

package com.taotao.cloud.sys.application.command.dept.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DeptConvertor;
import org.laokou.admin.domain.dept.Dept;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.dto.dept.DeptListQry;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * 查询部门列表执行器.
 *
 *
 */
@Component
@RequiredArgsConstructor
public class DeptListQryExe {

	private final DeptGateway deptGateway;

	private final DeptConvertor deptConvertor;

	/**
	 * 执行查询部门列表.
	 * @param qry 查询部门列表参数
	 * @return 部门列表
	 */
	@DS(TENANT)
	public Result<List<DeptCO>> execute(DeptListQry qry) {
		Dept dept = new Dept();
		dept.setName(qry.getName());
		List<Dept> list = deptGateway.list(dept);
		return Result.of(deptConvertor.convertClientObjectList(list));
	}

}
