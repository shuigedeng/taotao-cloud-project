

package com.taotao.cloud.sys.application.command.dict.dto;

import com.taotao.boot.ddd.application.model.CommonCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DictGetQry", description = "查看字典命令请求")
public class DictGetQry extends CommonCommand {

	@Schema(name = "id", description = "ID")
	private Long id;

}
