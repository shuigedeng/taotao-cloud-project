

package com.taotao.cloud.sys.application.command.dept.executor.query;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.application.command.dept.dto.DeptIDSGetQry;
import com.taotao.cloud.sys.domain.dept.service.DeptDomainService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * 查看部门IDS执行器.
 *
 *
 */
@Component
@RequiredArgsConstructor
public class DeptIDSGetQryExe {

	private final DeptDomainService deptDomainService;

	/**
	 * 执行查看部门IDS.
	 * @param qry 查看部门IDS参数
	 * @return 部门IDS
	 */
//	@DS(TENANT)
	public List<Long> execute(DeptIDSGetQry qry) {
//		return deptGateway.getDeptIds(qry.getRoleId());
		return null;
	}

}
