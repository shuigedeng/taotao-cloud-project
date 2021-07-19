package com.taotao.cloud.uc.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import lombok.*;

/**
 * shuigedeng
 *
 * @author shuigedeng
 * @since 2020/6/15 11:00
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DeptDTO", description = "用户注册DTO")
public class DeptDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Schema(description = "部门id", required = true)
	private Integer deptId;

	@Schema(description = "部门名称")
	private String name;

	@Schema(description = "上级部门id")
	private Integer parentId;

	@Schema(description = "排序")
	private Integer sort;

	@Schema(description = "备注")
	private String remark;


}
