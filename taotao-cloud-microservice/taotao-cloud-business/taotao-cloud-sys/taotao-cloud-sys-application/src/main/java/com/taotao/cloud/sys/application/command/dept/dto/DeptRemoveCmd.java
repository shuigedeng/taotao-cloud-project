
package com.taotao.cloud.sys.application.command.dept.dto;

import com.taotao.boot.ddd.application.model.CommonCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DeptDeleteCmd", description = "删除部门命令请求")
public class DeptRemoveCmd extends CommonCommand {

	@Schema(name = "ids", description = "IDS")
	private Long[] ids;

}
