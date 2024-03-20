

package com.taotao.cloud.auth.application.command.oauth2.dto;

import com.taotao.cloud.auth.application.command.oauth2.dto.clientobject.DictCO;
import com.taotao.cloud.ddd.application.model.CommonCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "DictInsertCmd", description = "新增字典命令请求")
public class DictInsertCmd extends CommonCommand {

	@Schema(name = "dictCO", description = "字典")
	private DictCO dictCO;

}
