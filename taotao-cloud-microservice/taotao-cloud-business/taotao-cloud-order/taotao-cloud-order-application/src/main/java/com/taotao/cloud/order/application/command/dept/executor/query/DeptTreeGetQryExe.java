

package com.taotao.cloud.sys.application.command.dept.executor.query;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.application.command.dept.dto.DeptTreeGetQry;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import com.taotao.cloud.sys.application.converter.DeptConvert;
import com.taotao.cloud.sys.domain.dept.service.DeptDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 查看部门树执行器.
 *
 *
 */
@Component
@RequiredArgsConstructor
public class DeptTreeGetQryExe {

	private final DeptDomainService deptDomainService;

	private final DeptConvert deptConvertor;

	/**
	 * 执行查看部门树.
	 * @param qry 查看部门树参数
	 * @return 部门树
	 */
//	@DS(TENANT)
	public DeptCO execute(DeptTreeGetQry qry) {
//		List<Dept> list = deptGateway.list(new Dept());
//		List<DeptCO> deptList = deptConvertor.convertClientObjectList(list);
//		return TreeUtil.buildTreeNode(deptList, DeptCO.class);
		return null;
	}

}
