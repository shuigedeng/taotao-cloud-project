

package com.taotao.cloud.sys.application.command.dept.dto;

import com.taotao.cloud.ddd.application.model.CommonCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
@Schema(name = "DeptListQry", description = "查询部门列表命令请求")
public class DeptListQry extends CommonCommand {

	@Schema(name = "name", description = "部门名称")
	private String name;

}
