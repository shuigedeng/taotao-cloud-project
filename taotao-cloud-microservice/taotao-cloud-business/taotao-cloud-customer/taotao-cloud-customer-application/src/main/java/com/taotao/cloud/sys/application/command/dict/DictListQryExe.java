

package com.taotao.cloud.sys.application.command.dict;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DictConvertor;
import org.laokou.admin.domain.dict.Dict;
import org.laokou.admin.domain.gateway.DictGateway;
import org.laokou.admin.dto.dict.DictListQry;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.common.core.utils.ConvertUtil;
import org.laokou.common.i18n.dto.Datas;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 查询部门列表执行器.
 *
 * 
 */
@Component
@RequiredArgsConstructor
public class DictListQryExe {

	private final DictGateway dictGateway;

	private final DictConvertor dictConvertor;

	/**
	 * 执行查询部门列表.
	 * @param qry 查询部门列表参数
	 * @return 部门列表
	 */
	@DS(TENANT)
	public Result<Datas<DictCO>> execute(DictListQry qry) {
		Dict dict = ConvertUtil.sourceToTarget(qry, Dict.class);
		Datas<Dict> datas = dictGateway.list(dict, qry);
		Datas<DictCO> da = new Datas<>();
		da.setRecords(dictConvertor.convertClientObjectList(datas.getRecords()));
		da.setTotal(datas.getTotal());
		return Result.of(da);
	}

}
