

package com.taotao.cloud.auth.application.command.oauth2.executor.query;

import com.taotao.cloud.auth.application.adapter.DictAdapter;
import com.taotao.cloud.auth.application.command.oauth2.dto.DictGetQry;
import com.taotao.cloud.auth.application.command.oauth2.dto.clientobject.DictCO;
import com.taotao.cloud.auth.application.converter.DictConvert;
import com.taotao.cloud.auth.domain.dict.service.DictDomainService;
import com.taotao.cloud.auth.infrastructure.persistent.dict.mapper.DictMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * 查看字典执行器.
 */
@Component
@RequiredArgsConstructor
public class DictGetQryExe {

	private final DictDomainService dictDomainService;
	private final DictAdapter dictAdapter;
	private final DictConvert dictConvert;
	private final DictMapper dictMapper;

	/**
	 * 执行查看字典.
	 * @param qry 查看字典参数
	 * @return 字典
	 */
	//@DS(TENANT)
	public DictCO execute(DictGetQry qry) {
		return dictConvert.convert(dictDomainService.getById(qry.getId()));
	}

}
