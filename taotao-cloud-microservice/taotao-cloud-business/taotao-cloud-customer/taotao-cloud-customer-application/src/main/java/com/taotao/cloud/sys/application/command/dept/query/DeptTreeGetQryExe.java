

package com.taotao.cloud.sys.application.command.dept.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DeptConvertor;
import org.laokou.admin.domain.dept.Dept;
import org.laokou.admin.domain.gateway.DeptGateway;
import org.laokou.admin.dto.dept.DeptTreeGetQry;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.common.core.utils.TreeUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

/**
 * 查看部门树执行器.
 *
 * 
 */
@Component
@RequiredArgsConstructor
public class DeptTreeGetQryExe {

	private final DeptGateway deptGateway;

	private final DeptConvertor deptConvertor;

	/**
	 * 执行查看部门树.
	 * @param qry 查看部门树参数
	 * @return 部门树
	 */
	@DS(TENANT)
	public Result<DeptCO> execute(DeptTreeGetQry qry) {
		List<Dept> list = deptGateway.list(new Dept());
		List<DeptCO> deptList = deptConvertor.convertClientObjectList(list);
		return Result.of(TreeUtil.buildTreeNode(deptList, DeptCO.class));
	}

}
