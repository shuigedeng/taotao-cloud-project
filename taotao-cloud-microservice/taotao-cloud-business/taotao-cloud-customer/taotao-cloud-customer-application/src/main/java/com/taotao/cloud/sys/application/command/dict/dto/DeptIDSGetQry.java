

package com.taotao.cloud.sys.application.command.dict.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DeptIDSGetQry", description = "查看部门IDS命令请求")
public class DeptIDSGetQry extends CommonCommand {

	@Schema(name = "roleId", description = "角色ID")
	private Long roleId;

}
