
package com.taotao.cloud.sys.application.service.impl;

import com.taotao.cloud.sys.application.command.dept.dto.DeptDeleteCmd;
import com.taotao.cloud.sys.application.command.dept.dto.DeptGetQry;
import com.taotao.cloud.sys.application.command.dept.dto.DeptIDSGetQry;
import com.taotao.cloud.sys.application.command.dept.dto.DeptInsertCmd;
import com.taotao.cloud.sys.application.command.dept.dto.DeptListQry;
import com.taotao.cloud.sys.application.command.dept.dto.DeptTreeGetQry;
import com.taotao.cloud.sys.application.command.dept.executor.DeptDeleteCmdExe;
import com.taotao.cloud.sys.application.command.dept.executor.query.DeptGetQryExe;
import com.taotao.cloud.sys.application.command.dept.executor.query.DeptIDSGetQryExe;
import com.taotao.cloud.sys.application.command.dept.executor.DeptInsertCmdExe;
import com.taotao.cloud.sys.application.command.dept.executor.query.DeptListQryExe;
import com.taotao.cloud.sys.application.command.dept.executor.query.DeptTreeGetQryExe;
import com.taotao.cloud.sys.application.command.dept.executor.DeptUpdateCmdExe;
import com.taotao.cloud.sys.application.command.dept.dto.DeptUpdateCmd;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import com.taotao.cloud.sys.application.service.DeptsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门管理.
 */
@Service
@RequiredArgsConstructor
public class DeptsServiceImpl implements DeptsService {

	private final DeptTreeGetQryExe deptTreeGetQryExe;

	private final DeptListQryExe deptListQryExe;

	private final DeptInsertCmdExe deptInsertCmdExe;

	private final DeptUpdateCmdExe deptUpdateCmdExe;

	private final DeptDeleteCmdExe deptDeleteCmdExe;

	private final DeptGetQryExe deptGetQryExe;

	private final DeptIDSGetQryExe deptIDSGetQryExe;

	/**
	 * 查看部门树.
	 * @param qry 查看部门树参数
	 * @return 部门树
	 */
	@Override
	public DeptCO tree(DeptTreeGetQry qry) {
		return deptTreeGetQryExe.execute(qry);
	}

	/**
	 * 查询部门列表.
	 * @param qry 查询部门列表参数
	 * @return 部门列表
	 */
	@Override
	public List<DeptCO> list(DeptListQry qry) {
		return deptListQryExe.execute(qry);
	}

	/**
	 * 新增部门.
	 * @param cmd 新增部门参数
	 * @return 新增结果
	 */
	@Override
	public Boolean insert(DeptInsertCmd cmd) {
		return deptInsertCmdExe.execute(cmd);
	}

	/**
	 * 修改部门.
	 * @param cmd 修改部门参数
	 * @return 修改结果
	 */
	@Override
	public Boolean update(DeptUpdateCmd cmd) {
		return deptUpdateCmdExe.execute(cmd);
	}

	/**
	 * 根据ID删除部门.
	 * @param cmd 根据ID删除部门参数
	 * @return 删除结果
	 */
	@Override
	public Boolean deleteById(DeptDeleteCmd cmd) {
		return deptDeleteCmdExe.execute(cmd);
	}

	/**
	 * 根据ID查看部门.
	 * @param qry 根据ID查看部门参数
	 * @return 部门
	 */
	@Override
	public DeptCO getById(DeptGetQry qry) {
		return deptGetQryExe.execute(qry);
	}

	/**
	 * 根据角色ID查看部门IDS.
	 * @param qry 根据角色ID查看部门IDS参数
	 * @return 部门IDS
	 */
	@Override
	public List<Long> ids(DeptIDSGetQry qry) {
		return deptIDSGetQryExe.execute(qry);
	}

}
