

package com.taotao.cloud.sys.application.command.dict;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DictConvertor;
import org.laokou.admin.domain.gateway.DictGateway;
import org.laokou.admin.dto.dict.DictGetQry;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 查看字典执行器.
 *
 * 
 */
@Component
@RequiredArgsConstructor
public class DictGetQryExe {

	private final DictGateway dictGateway;

	private final DictConvertor dictConvertor;

	/**
	 * 执行查看字典.
	 * @param qry 查看字典参数
	 * @return 字典
	 */
	@DS(TENANT)
	public Result<DictCO> execute(DictGetQry qry) {
		return Result.of(dictConvertor.convertClientObject(dictGateway.getById(qry.getId())));
	}

}
