
package com.taotao.cloud.sys.application.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.boot.common.model.Result;
import com.taotao.cloud.sys.application.command.dict.dto.DictDeleteCmd;
import com.taotao.cloud.sys.application.command.dict.dto.DictGetQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictInsertCmd;
import com.taotao.cloud.sys.application.command.dict.dto.DictListQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictOptionListQry;
import com.taotao.cloud.sys.application.command.dict.dto.DictUpdateCmd;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.DictCO;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.OptionCO;
import com.taotao.cloud.sys.application.command.dict.executor.DictDeleteCmdExe;
import com.taotao.cloud.sys.application.command.dict.executor.DictInsertCmdExe;
import com.taotao.cloud.sys.application.command.dict.executor.DictUpdateCmdExe;
import com.taotao.cloud.sys.application.command.dict.executor.query.DictGetQryExe;
import com.taotao.cloud.sys.application.command.dict.executor.query.DictListQryExe;
import com.taotao.cloud.sys.application.command.dict.executor.query.DictOptionListQryExe;
import com.taotao.cloud.sys.application.service.DictsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 字典管理.
 */
@Service
@RequiredArgsConstructor
public class DictsServiceImpl implements DictsService {

	private final DictInsertCmdExe dictInsertCmdExe;

	private final DictUpdateCmdExe dictUpdateCmdExe;

	private final DictDeleteCmdExe dictDeleteCmdExe;

	private final DictOptionListQryExe dictOptionListQryExe;

	private final DictGetQryExe dictGetQryExe;

	private final DictListQryExe dictListQryExe;

	/**
	 * 新增字典.
	 *
	 * @param cmd 新增字典参数
	 * @return 新增结果
	 */
	@Override
	public Boolean insert(DictInsertCmd cmd) {
		return dictInsertCmdExe.execute(cmd);
	}

	/**
	 * 修改字典.
	 *
	 * @param cmd 修改字典参数
	 * @return 修改结果
	 */
	@Override
	public Boolean update(DictUpdateCmd cmd) {
		return dictUpdateCmdExe.execute(cmd);
	}

	/**
	 * 根据ID删除字典.
	 *
	 * @param cmd 根据ID删除字典参数
	 * @return 删除字典
	 */
	@Override
	public Boolean deleteById(DictDeleteCmd cmd) {
		return dictDeleteCmdExe.execute(cmd);
	}

	/**
	 * 根据ID查看字典.
	 *
	 * @param qry 根据ID查看字典参数
	 * @return 字典
	 */
	@Override
	public DictCO getById(DictGetQry qry) {
		return dictGetQryExe.execute(qry);
	}

	/**
	 * 查询字典下拉框选择项列表.
	 *
	 * @param qry 查询字典下拉框选择项列表参数
	 * @return 字典下拉框选择项列表
	 */
	@Override
	public List<OptionCO> optionList(DictOptionListQry qry) {
		return dictOptionListQryExe.execute(qry);
	}

	/**
	 * 查询字典列表.
	 *
	 * @param qry 查询字典列表参数
	 * @return 字典列表
	 */
	@Override
	public IPage<DictCO> list(DictListQry qry) {
		return dictListQryExe.execute(qry);
	}


}
