package com.taotao.cloud.sys.application.service;

import com.taotao.cloud.sys.application.command.dept.dto.DeptDeleteCmd;
import com.taotao.cloud.sys.application.command.dept.dto.DeptGetQry;
import com.taotao.cloud.sys.application.command.dept.dto.DeptIDSGetQry;
import com.taotao.cloud.sys.application.command.dept.dto.DeptInsertCmd;
import com.taotao.cloud.sys.application.command.dept.dto.DeptListQry;
import com.taotao.cloud.sys.application.command.dept.dto.DeptTreeGetQry;
import com.taotao.cloud.sys.application.command.dept.dto.DeptUpdateCmd;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import java.util.List;

/**
 * 部门管理.
 *
 *
 */
public interface DeptsService {

	/**
	 * 查看部门树.
	 *
	 * @param qry 查看部门树参数
	 * @return 部门树
	 */
	DeptCO tree(DeptTreeGetQry qry);

	/**
	 * 查询部门列表.
	 *
	 * @param qry 查询部门列表参数
	 * @return 部门列表
	 */
	List<DeptCO> list(DeptListQry qry);

	/**
	 * 新增部门.
	 *
	 * @param cmd 新增部门参数
	 * @return 新增结果
	 */
	Boolean insert(DeptInsertCmd cmd);

	/**
	 * 修改部门.
	 *
	 * @param cmd 修改部门参数
	 * @return 修改参数
	 */
	Boolean update(DeptUpdateCmd cmd);

	/**
	 * 根据ID删除部门.
	 *
	 * @param cmd 根据ID删除部门参数
	 * @return 删除结果
	 */
	Boolean deleteById(DeptDeleteCmd cmd);

	/**
	 * 根据ID查看部门.
	 *
	 * @param qry 根据ID查看部门参数
	 * @return 部门
	 */
	DeptCO getById(DeptGetQry qry);

	/**
	 * 根据角色ID查看部门IDS.
	 *
	 * @param qry 根据角色ID查看部门IDS参数
	 * @return 部门IDS
	 */
	List<Long> ids(DeptIDSGetQry qry);

}
