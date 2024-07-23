

package com.taotao.cloud.order.application.command.store.executor.query;


import com.taotao.cloud.sys.application.command.dept.dto.DeptGetQry;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import com.taotao.cloud.sys.application.converter.DeptConvert;
import com.taotao.cloud.sys.domain.dept.service.DeptDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 查看部门执行器.
 *
 *
 */
@Component
@RequiredArgsConstructor
public class StorePageQryExe {

	private final DeptDomainService deptDomainService;

	private final DeptConvert memberNoticeConvertor;

	/**
	 * 执行查看部门.
	 *
	 * @param qry 查看部门参数
	 * @return 部门
	 */
//	@DS(TENANT)
	public DeptCO execute(DeptGetQry qry) {
//		return deptConvertor.convertClientObject(deptGateway.getById(qry.getId()));
		return null;
	}

}
