package com.taotao.cloud.uc.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 字典dto
 *
 * @author shuigedeng
 * @since 2020/4/30 11:20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DictDTO", description = "字典DTO")
public class DictDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "字典id")
	private Integer id;

	@Schema(description = "字典名称")
	private String dictName;

	@Schema(description = "字典code")
	private String dictCode;

	@Schema(description = "字典描述")
	private String description;

	@Schema(description = "字典排序")
	private Integer sort;

	@Schema(description = "字典备注")
	private String remark;

	@Schema(description = "字典值")
	private String value;
}
