

package com.taotao.cloud.sys.application.command.dict;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.dto.common.clientobject.OptionCO;
import org.laokou.admin.dto.dict.DictOptionListQry;
import org.laokou.admin.gatewayimpl.database.DictMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DictDO;
import org.laokou.common.core.utils.CollectionUtil;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 查询字典下拉框选择项列表执行器.
 *
 * 
 */
@Component
@RequiredArgsConstructor
public class DictOptionListQryExe {

	private final DictMapper dictMapper;

	/**
	 * 执行查询字典下拉框选择项列表.
	 * @param qry 查询字典下拉框选择项列表参数
	 * @return 字典下拉框选择项列表
	 */
	@DS(TENANT)
	public Result<List<OptionCO>> execute(DictOptionListQry qry) {
		List<DictDO> list = dictMapper.selectList(Wrappers.lambdaQuery(DictDO.class)
			.eq(DictDO::getType, qry.getType())
			.select(DictDO::getLabel, DictDO::getValue)
			.orderByDesc(DictDO::getId));
		if (CollectionUtil.isEmpty(list)) {
			return Result.of(new ArrayList<>(0));
		}
		return Result.of(ConvertUtil.sourceToTarget(list, OptionCO.class));
	}

}
