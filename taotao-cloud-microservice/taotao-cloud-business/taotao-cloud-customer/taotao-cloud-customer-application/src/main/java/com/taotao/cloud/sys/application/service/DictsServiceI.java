

package com.taotao.cloud.sys.application.service;

import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.admin.dto.dict.*;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;

import java.util.List;

/**
 * 字典管理.
 *
 *
 */
public interface DictsServiceI {

	/**
	 * 新增字典.
	 * @param cmd 新增字典参数
	 * @return 新增结果
	 */
	Result<Boolean> insert(DictInsertCmd cmd);

	/**
	 * 修改字典.
	 * @param cmd 修改字典参数
	 * @return 修改结果
	 */
	Result<Boolean> update(DictUpdateCmd cmd);

	/**
	 * 根据ID删除字典.
	 * @param cmd 根据ID删除字典参数
	 * @return 删除结果
	 */
	Result<Boolean> deleteById(DictDeleteCmd cmd);

	/**
	 * 根据ID查看字典.
	 * @param qry 根据ID查看字典参数
	 * @return 字典
	 */
	Result<DictCO> getById(DictGetQry qry);

	/**
	 * 查询字典下拉框选择项列表.
	 * @param qry 查询字典下拉框选择项列表参数
	 * @return 字典下拉框选择项列表
	 */
	Result<List<OptionCO>> optionList(DictOptionListQry qry);

	/**
	 * 查询字典列表.
	 * @param qry 查询字典列表参数
	 * @return 字典列表
	 */
	Result<Datas<DictCO>> list(DictListQry qry);

}
