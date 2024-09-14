

package com.taotao.cloud.sys.application.command.dict.executor;

import com.taotao.boot.common.model.Result;
import com.taotao.cloud.sys.application.adapter.DictAdapter;
import com.taotao.cloud.sys.application.command.dict.dto.DictDeleteCmd;
import com.taotao.cloud.sys.application.converter.DictConvert;
import com.taotao.cloud.sys.domain.dict.service.DictDomainService;
import com.taotao.cloud.sys.infrastructure.persistent.dict.mapper.DictMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * 删除字典执行器.
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DictDeleteCmdExe {

	private final DictDomainService dictDomainService;
	//private final DictAdapter dictAdapter;
	//private final DictConvert dictConvert;
	private final DictMapper dictMapper;

	/**
	 * 执行删除字典.
	 * @param cmd 删除字典参数
	 * @return 执行删除结果
	 */
//	@DS(TENANT)
	public Boolean execute(DictDeleteCmd cmd) {
		return dictDomainService.deleteById(cmd.getId());
	}

}
