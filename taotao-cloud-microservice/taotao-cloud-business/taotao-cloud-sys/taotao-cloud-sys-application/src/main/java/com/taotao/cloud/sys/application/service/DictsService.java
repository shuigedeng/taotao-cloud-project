

package com.taotao.cloud.sys.application.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.sys.application.command.dict.dto.DictDeleteCmd;
import com.taotao.cloud.sys.application.command.dict.dto.DictGetQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictInsertCmd;
import com.taotao.cloud.sys.application.command.dict.dto.DictListQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictOptionListQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictUpdateCmd;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.DictCO;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.OptionCO;
import java.util.List;

/**
 * 字典管理.
 */
public interface DictsService {


	/**
	 * 新增字典.
	 *
	 * @param cmd 新增字典参数
	 * @return 新增结果
	 */
	Boolean insert(DictInsertCmd cmd);

	/**
	 * 修改字典.
	 *
	 * @param cmd 修改字典参数
	 * @return 修改结果
	 */
	Boolean update(DictUpdateCmd cmd);

	/**
	 * 根据ID删除字典.
	 *
	 * @param cmd 根据ID删除字典参数
	 * @return 删除结果
	 */
	Boolean deleteById(DictDeleteCmd cmd);

	/**
	 * 根据ID查看字典.
	 *
	 * @param qry 根据ID查看字典参数
	 * @return 字典
	 */
	DictCO getById(DictGetQry qry);

	/**
	 * 查询字典下拉框选择项列表.
	 *
	 * @param qry 查询字典下拉框选择项列表参数
	 * @return 字典下拉框选择项列表
	 */
	List<OptionCO> optionList(DictOptionListQry qry);

	/**
	 * 查询字典列表.
	 *
	 * @param qry 查询字典列表参数
	 * @return 字典列表
	 */
	IPage<DictCO> list(DictListQry qry);

}
