

package com.taotao.cloud.sys.application.command.dict.dto;

import com.taotao.boot.ddd.application.model.CommonCommand;
import com.taotao.cloud.sys.application.command.dict.dto.clientobject.DictCO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "DictUpdateCmd", description = "修改字典命令请求")
public class DictUpdateCmd extends CommonCommand {

	@Schema(name = "dictCO", description = "字典")
	private DictCO dictCO;

}
