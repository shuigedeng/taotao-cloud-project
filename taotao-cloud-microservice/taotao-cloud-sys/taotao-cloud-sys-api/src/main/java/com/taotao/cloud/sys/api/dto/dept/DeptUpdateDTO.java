package com.taotao.cloud.sys.api.dto.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;

import lombok.*;

/**
 * 部门更新对象
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-23 08:50:21
 */
@Data
@Builder
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "部门更新对象")
public class DeptUpdateDTO implements Serializable {

	@Serial
	private static final long serialVersionUID = -4132785717179910025L;

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
