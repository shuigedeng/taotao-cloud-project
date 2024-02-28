

package com.taotao.cloud.sys.application.command.dept.dto.clientobject;

import static lombok.AccessLevel.PRIVATE;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = PRIVATE)
@NoArgsConstructor(access = PRIVATE)
@Schema(name = "DeptCO", description = "部门")
public class DeptCO {

	@Serial
	private static final long serialVersionUID = 4116703987840123059L;

	@Schema(name = "id", description = "ID")
	private Long id;

	@Schema(name = "name", description = "名称")
	private String name;

	@Schema(name = "pid", description = "父节点ID")
	private Long pid;

	@Schema(name = "path", description = "节点PATH")
	private String path;

	@Schema(name = "children", description = "子节点")
	private List<DeptCO> children = new ArrayList<>(16);

	@Schema(name = "sort", description = "部门排序")
	private Integer sort;


}
