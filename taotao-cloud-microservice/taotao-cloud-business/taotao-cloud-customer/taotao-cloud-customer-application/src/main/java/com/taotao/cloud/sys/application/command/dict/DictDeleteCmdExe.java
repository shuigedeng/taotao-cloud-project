

package com.taotao.cloud.sys.application.command.dict;

import static org.laokou.common.i18n.common.DatasourceConstants.TENANT;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.laokou.admin.domain.gateway.DictGateway;
import org.laokou.admin.dto.dict.DictDeleteCmd;
import org.laokou.common.i18n.dto.Result;
import org.springframework.stereotype.Component;

/**
 * 删除字典执行器.
 *
 * 
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DictDeleteCmdExe {

	private final DictGateway dictGateway;

	/**
	 * 执行删除字典.
	 * @param cmd 删除字典参数
	 * @return 执行删除结果
	 */
	@DS(TENANT)
	public Result<Boolean> execute(DictDeleteCmd cmd) {
		return Result.of(dictGateway.deleteById(cmd.getId()));
	}

}
