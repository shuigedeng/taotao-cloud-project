

package com.taotao.cloud.sys.application.command.dict.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.admin.dto.dept.clientobject.DeptCO;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 *
 */
@Data
@Schema(name = "DeptInsertCmd", description = "新增部门命令请求")
public class DeptInsertCmd extends CommonCommand {

	@Schema(name = "deptCO", description = "部门")
	private DeptCO deptCO;

}
