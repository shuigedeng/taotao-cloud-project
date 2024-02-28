

package com.taotao.cloud.sys.application.command.dict.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.CommonCommand;

/**
 *
 */
@Data
@Schema(name = "DeptUpdateCmd", description = "构建部门树命令请求")
public class DeptTreeGetQry extends CommonCommand {

}
