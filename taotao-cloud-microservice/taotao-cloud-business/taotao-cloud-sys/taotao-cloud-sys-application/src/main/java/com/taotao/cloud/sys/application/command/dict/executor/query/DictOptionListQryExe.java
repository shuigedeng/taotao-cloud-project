

package com.taotao.cloud.sys.application.command.dict.executor.query;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.boot.common.model.Result;
import com.taotao.cloud.sys.application.adapter.DictAdapter;
import com.taotao.cloud.sys.application.command.dict.dto.DictOptionListQry;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.OptionCO;
import com.taotao.cloud.sys.application.converter.DictConvert;
import com.taotao.cloud.sys.domain.dict.service.DictDomainService;
import com.taotao.cloud.sys.infrastructure.persistent.dict.dataobject.DictDO;
import com.taotao.cloud.sys.infrastructure.persistent.dict.mapper.DictMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * 查询字典下拉框选择项列表执行器.
 *
 */
@Component
@RequiredArgsConstructor
public class DictOptionListQryExe {

	private final DictDomainService dictDomainService;
	//private final DictAdapter dictAdapter;
	//private final DictConvert dictConvert;
	private final DictMapper dictMapper;

	/**
	 * 执行查询字典下拉框选择项列表.
	 * @param qry 查询字典下拉框选择项列表参数
	 * @return 字典下拉框选择项列表
	 */
	//@DS(TENANT)
	public List<OptionCO> execute(DictOptionListQry qry) {
//		List<DictDO> list = dictMapper.selectList(Wrappers.lambdaQuery(DictDO.class)
//			.eq(DictDO::getType, qry.getType())
//			.select(DictDO::getLabel, DictDO::getValue)
//			.orderByDesc(DictDO::getId));
//		if (CollectionUtil.isEmpty(list)) {
//			return new ArrayList<>(0);
//		}
//		return ConvertUtil.sourceToTarget(list, OptionCO.class);
		return null;
	}

}
