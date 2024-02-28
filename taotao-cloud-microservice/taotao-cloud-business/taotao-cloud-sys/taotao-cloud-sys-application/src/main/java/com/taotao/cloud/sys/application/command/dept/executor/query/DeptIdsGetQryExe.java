
package com.taotao.cloud.sys.application.command.dept.executor.query;

import com.taotao.cloud.sys.application.command.dept.dto.DeptIdsGetQry;
import com.taotao.cloud.sys.infrastructure.persistent.dept.mapper.DeptMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 查看部门IDS执行器.
 */
@Component
@RequiredArgsConstructor
public class DeptIdsGetQryExe {

	private final DeptMapper deptMapper;

	/**
	 * 执行查看部门IDS.
	 *
	 * @param qry 查看部门IDS参数
	 * @return 部门IDS
	 */
	//@DS(TENANT)
	public List<Long> execute(DeptIdsGetQry qry) {
		return deptMapper.selectIdsByRoleId(qry.getRoleId());
	}

}
