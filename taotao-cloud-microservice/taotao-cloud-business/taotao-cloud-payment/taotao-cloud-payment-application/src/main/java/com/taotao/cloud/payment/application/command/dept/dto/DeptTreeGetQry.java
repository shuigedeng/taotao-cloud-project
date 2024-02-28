

package com.taotao.cloud.sys.application.command.dept.dto;

import com.taotao.cloud.ddd.application.model.CommonCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 *
 */
@Data
@Schema(name = "DeptUpdateCmd", description = "构建部门树命令请求")
public class DeptTreeGetQry extends CommonCommand {

}
