

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
@Schema(name = "DeptDeleteCmd", description = "删除部门命令请求")
public class DeptDeleteCmd extends CommonCommand {

	@Schema(name = "id", description = "ID")
	private Long id;

}
