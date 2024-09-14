

package com.taotao.cloud.sys.application.command.dict.executor;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.taotao.boot.common.exception.BusinessException;
import com.taotao.boot.common.model.Result;
import com.taotao.cloud.sys.application.adapter.DictAdapter;
import com.taotao.cloud.sys.application.command.dict.dto.DictInsertCmd;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.DictCO;
import com.taotao.cloud.sys.application.converter.DictConvert;
import com.taotao.cloud.sys.domain.dict.service.DictDomainService;
import com.taotao.cloud.sys.infrastructure.persistent.dict.dataobject.DictDO;
import com.taotao.cloud.sys.infrastructure.persistent.dict.mapper.DictMapper;
import jakarta.transaction.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 新增字典执行器.
 *
 */
@Component
@RequiredArgsConstructor
public class DictInsertCmdExe {

	private final DictDomainService dictDomainService;
	//private final DictAdapter dictAdapter;
	//private final DictConvert dictConvert;
	private final DictMapper dictMapper;

	/**
	 * 执行新增字典.
	 * @param cmd 新增字典参数
	 * @return 执行新增结果
	 */
//	@DS(TENANT)
	public Boolean execute(DictInsertCmd cmd) {
//		DictCO co = cmd.getDictCO();
//		String type = co.getType();
//		String value = co.getValue();
//		Long count = dictMapper
//			.selectCount(Wrappers.lambdaQuery(DictDO.class).eq(DictDO::getValue, value).eq(DictDO::getType, type));
//		if (count > 0) {
//			throw new BusinessException(String.format("类型为%s，值为%s的字典已存在，请重新填写", type, value));
//		}
//		return dictDomainService.insert(dictConvertor.toEntity(co));
		return null;
	}

}
