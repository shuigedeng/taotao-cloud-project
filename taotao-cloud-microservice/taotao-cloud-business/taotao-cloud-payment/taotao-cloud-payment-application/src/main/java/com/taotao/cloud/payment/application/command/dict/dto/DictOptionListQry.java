

package com.taotao.cloud.sys.application.command.dict.dto;

import com.taotao.boot.ddd.application.model.CommonCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DictOptionListQry", description = "查询部门下拉框选择项命令请求")
public class DictOptionListQry extends CommonCommand {

	@Schema(name = "type", description = "字典类型")
	private String type;

}
