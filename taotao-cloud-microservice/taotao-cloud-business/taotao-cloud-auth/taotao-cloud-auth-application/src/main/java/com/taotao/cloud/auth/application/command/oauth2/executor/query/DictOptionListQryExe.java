

package com.taotao.cloud.auth.application.command.oauth2.executor.query;

import com.taotao.cloud.auth.application.adapter.DictAdapter;
import com.taotao.cloud.auth.application.command.oauth2.dto.DictOptionListQry;
import com.taotao.cloud.auth.application.command.oauth2.dto.clientobject.OptionCO;
import com.taotao.cloud.auth.application.converter.DictConvert;
import com.taotao.cloud.auth.domain.dict.service.DictDomainService;
import com.taotao.cloud.auth.infrastructure.persistent.dict.mapper.DictMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 查询字典下拉框选择项列表执行器.
 *
 */
@Component
@RequiredArgsConstructor
public class DictOptionListQryExe {

	private final DictDomainService dictDomainService;
	private final DictAdapter dictAdapter;
	private final DictConvert dictConvert;
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
