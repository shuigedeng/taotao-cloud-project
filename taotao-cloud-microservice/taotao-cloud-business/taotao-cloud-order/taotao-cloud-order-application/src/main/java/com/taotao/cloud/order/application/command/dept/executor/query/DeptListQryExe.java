

package com.taotao.cloud.sys.application.command.dept.executor.query;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.application.command.dept.dto.DeptListQry;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import com.taotao.cloud.sys.application.converter.DeptConvert;
import com.taotao.cloud.sys.domain.dept.service.DeptDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 查询部门列表执行器.
 *
 *
 */
@Component
@RequiredArgsConstructor
public class DeptListQryExe {

	private final DeptDomainService deptDomainService;

	private final DeptConvert deptConvertor;

	/**
	 * 执行查询部门列表.
	 * @param qry 查询部门列表参数
	 * @return 部门列表
	 */
//	@DS(TENANT)
	public List<DeptCO> execute(DeptListQry qry) {
//		Dept dept = new Dept();
//		dept.setName(qry.getName());
//		List<Dept> list = deptGateway.list(dept);
//		return deptConvertor.convertClientObjectList(list);
		return null;
	}

}
