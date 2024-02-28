

package com.taotao.cloud.sys.application.command.dict.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(name = "DictCO", description = "字典")
public class DictCO  {

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "label", description = "字典标签")
	private String label;

	@Schema(name = "type", description = "字典类型")
	private String type;

	@Schema(name = "value", description = "字典值")
	private String value;

	@Schema(name = "remark", description = "字典备注")
	private String remark;

	@Schema(name = "createDate", description = "创建时间")
	private LocalDateTime createDate;

	@Schema(name = "sort", description = "字典排序")
	private Integer sort;

}
