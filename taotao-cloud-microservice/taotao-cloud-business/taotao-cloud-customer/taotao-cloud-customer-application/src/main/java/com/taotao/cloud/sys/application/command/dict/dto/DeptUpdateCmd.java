

package com.taotao.cloud.sys.application.command.dict.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DeptUpdateCmd", description = "修改部门命令请求")
public class DeptUpdateCmd extends CommonCommand {

	@Schema(name = "deptCO", description = "部门")
	private DeptCO deptCO;

}
