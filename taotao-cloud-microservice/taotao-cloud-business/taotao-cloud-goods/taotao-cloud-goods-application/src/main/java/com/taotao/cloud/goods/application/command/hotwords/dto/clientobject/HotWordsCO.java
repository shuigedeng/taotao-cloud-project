

package com.taotao.cloud.goods.application.command.hotwords.dto.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
@Data
@Schema(name = "DeptCO", description = "部门")
//public class DeptCO extends TreeUtil.TreeNode<DeptCO> {
public class HotWordsCO {

	@Serial
	private static final long serialVersionUID = 4116703987840123059L;

	@Schema(name = "sort", description = "部门排序")
	private Integer sort;

}
