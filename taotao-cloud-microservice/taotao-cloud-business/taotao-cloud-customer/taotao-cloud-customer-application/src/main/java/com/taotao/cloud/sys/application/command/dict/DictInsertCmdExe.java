

package com.taotao.cloud.sys.application.command.dict;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.laokou.admin.convertor.DictConvertor;
import org.laokou.admin.domain.gateway.DictGateway;
import org.laokou.admin.dto.dict.DictInsertCmd;
import org.laokou.admin.dto.dict.clientobject.DictCO;
import org.laokou.admin.gatewayimpl.database.DictMapper;
import org.laokou.admin.gatewayimpl.database.dataobject.DictDO;
import org.laokou.common.i18n.common.exception.SystemException;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 新增字典执行器.
 *
 * 
 */
@Component
@RequiredArgsConstructor
public class DictInsertCmdExe {

	private final DictGateway dictGateway;

	private final DictMapper dictMapper;

	private final DictConvertor dictConvertor;

	/**
	 * 执行新增字典.
	 * @param cmd 新增字典参数
	 * @return 执行新增结果
	 */
	@DS(TENANT)
	public Result<Boolean> execute(DictInsertCmd cmd) {
		DictCO co = cmd.getDictCO();
		String type = co.getType();
		String value = co.getValue();
		Long count = dictMapper
			.selectCount(Wrappers.lambdaQuery(DictDO.class).eq(DictDO::getValue, value).eq(DictDO::getType, type));
		if (count > 0) {
			throw new SystemException(String.format("类型为%s，值为%s的字典已存在，请重新填写", type, value));
		}
		return Result.of(dictGateway.insert(dictConvertor.toEntity(co)));
	}

}
