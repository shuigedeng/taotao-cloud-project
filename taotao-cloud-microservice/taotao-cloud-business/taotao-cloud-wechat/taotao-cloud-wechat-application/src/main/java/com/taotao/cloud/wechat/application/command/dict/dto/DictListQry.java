

package com.taotao.cloud.sys.application.command.dict.dto;

import com.taotao.boot.common.model.PageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 
 */
@Data
@Schema(name = "DictListQry", description = "字典列表查询参数")
public class DictListQry extends PageQuery {

	@Schema(name = "type", description = "字典类型")
	private String type;

	@Schema(name = "label", description = "字典标签")
	private String label;

}
