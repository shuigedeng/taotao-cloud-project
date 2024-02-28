package com.taotao.cloud.sys.application.service;

import com.taotao.cloud.sys.application.command.dept.dto.DeptCreateCmd;
import com.taotao.cloud.sys.application.command.dept.dto.DeptGetQry;
import com.taotao.cloud.sys.application.command.dept.dto.DeptIdsGetQry;
import com.taotao.cloud.sys.application.command.dept.dto.DeptListQry;
import com.taotao.cloud.sys.application.command.dept.dto.DeptModifyCmd;
import com.taotao.cloud.sys.application.command.dept.dto.DeptRemoveCmd;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import java.util.List;

/**
 * 部门管理.
 */
public interface DeptsService {


	/**
	 * 查询部门列表.
	 *
	 * @param qry 查询部门列表参数
	 * @return 部门列表
	 */
	List<DeptCO> findList(DeptListQry qry);

	/**
	 * 新增部门.
	 *
	 * @param cmd 新增部门参数
	 */
	void create(DeptCreateCmd cmd);

	/**
	 * 修改部门.
	 *
	 * @param cmd 修改部门参数
	 */
	void modify(DeptModifyCmd cmd);

	/**
	 * 根据IDS删除部门.
	 *
	 * @param cmd 根据IDS删除部门参数
	 */
	void remove(DeptRemoveCmd cmd);

	/**
	 * 根据ID查看部门.
	 *
	 * @param qry 根据ID查看部门参数
	 * @return 部门
	 */
	DeptCO findById(DeptGetQry qry);

	/**
	 * 根据角色ID查看部门IDS.
	 *
	 * @param qry 根据角色ID查看部门IDS参数
	 * @return 部门IDS
	 */
	List<Long> findIds(DeptIdsGetQry qry);

}
