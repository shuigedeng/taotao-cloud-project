package com.taotao.cloud.sys.application.command.dept.dto;

import com.taotao.boot.ddd.application.model.CommonCommand;
import com.taotao.cloud.sys.application.command.dept.dto.clientobject.DeptCO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(name = "DeptInsertCmd", description = "新增部门命令请求")
public class DeptCreateCmd extends CommonCommand {

	@Schema(name = "deptCO", description = "部门")
	private DeptCO deptCO;

}
