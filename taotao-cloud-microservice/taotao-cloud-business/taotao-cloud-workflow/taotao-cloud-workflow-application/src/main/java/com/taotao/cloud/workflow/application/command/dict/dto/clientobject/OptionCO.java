

package com.taotao.cloud.sys.application.command.dict.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;

/**
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "OptionCO", description = "下拉框选择参数项参数")
public class OptionCO  {

	@Serial
	private static final long serialVersionUID = -4146348495335527374L;

	@Schema(name = "label", description = "标签")
	private String label;

	@Schema(name = "value", description = "值")
	private String value;

}
