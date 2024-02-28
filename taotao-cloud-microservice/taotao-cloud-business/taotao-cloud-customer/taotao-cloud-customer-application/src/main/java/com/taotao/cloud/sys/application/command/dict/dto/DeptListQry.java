

package com.taotao.cloud.sys.application.command.dict.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.laokou.common.i18n.dto.CommonCommand;
import org.laokou.common.i18n.utils.StringUtil;

/**
 * 
 */
@Data
@Schema(name = "DeptListQry", description = "查询部门列表命令请求")
public class DeptListQry extends CommonCommand {

	@Schema(name = "name", description = "部门名称")
	private String name;

	public void setName(String name) {
		this.name = StringUtil.like(name);
	}

}
