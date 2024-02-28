

package com.taotao.cloud.sys.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.laokou.admin.api.DictsServiceI;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.dict.*;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.admin.command.dict.DictDeleteCmdExe;
import org.laokou.admin.command.dict.DictInsertCmdExe;
import org.laokou.admin.command.dict.DictUpdateCmdExe;
import org.laokou.admin.command.dict.query.DictGetQryExe;
import org.laokou.admin.command.dict.query.DictListQryExe;
import org.laokou.admin.command.dict.query.DictOptionListQryExe;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 字典管理.
 *
 * 
 */
@Service
@RequiredArgsConstructor
public class DictsServiceImpl implements DictsServiceI {

	private final DictInsertCmdExe dictInsertCmdExe;

	private final DictUpdateCmdExe dictUpdateCmdExe;

	private final DictDeleteCmdExe dictDeleteCmdExe;

	private final DictOptionListQryExe dictOptionListQryExe;

	private final DictGetQryExe dictGetQryExe;

	private final DictListQryExe dictListQryExe;

	/**
	 * 新增字典.
	 * @param cmd 新增字典参数
	 * @return 新增结果
	 */
	@Override
	public Result<Boolean> insert(DictInsertCmd cmd) {
		return dictInsertCmdExe.execute(cmd);
	}

	/**
	 * 修改字典.
	 * @param cmd 修改字典参数
	 * @return 修改结果
	 */
	@Override
	public Result<Boolean> update(DictUpdateCmd cmd) {
		return dictUpdateCmdExe.execute(cmd);
	}

	/**
	 * 根据ID删除字典.
	 * @param cmd 根据ID删除字典参数
	 * @return 删除字典
	 */
	@Override
	public Result<Boolean> deleteById(DictDeleteCmd cmd) {
		return dictDeleteCmdExe.execute(cmd);
	}

	/**
	 * 根据ID查看字典.
	 * @param qry 根据ID查看字典参数
	 * @return 字典
	 */
	@Override
	public Result<DictCO> getById(DictGetQry qry) {
		return dictGetQryExe.execute(qry);
	}

	/**
	 * 查询字典下拉框选择项列表.
	 * @param qry 查询字典下拉框选择项列表参数
	 * @return 字典下拉框选择项列表
	 */
	@Override
	public Result<List<OptionCO>> optionList(DictOptionListQry qry) {
		return dictOptionListQryExe.execute(qry);
	}

	/**
	 * 查询字典列表.
	 * @param qry 查询字典列表参数
	 * @return 字典列表
	 */
	@Override
	public Result<Datas<DictCO>> list(DictListQry qry) {
		return dictListQryExe.execute(qry);
	}

}
