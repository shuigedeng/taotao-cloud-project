

package com.taotao.cloud.goods.application.command.dict.clientobject;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import lombok.Data;
import org.laokou.common.core.utils.TreeUtil;

/**
 *
 */
@Data
@Schema(name = "DeptCO", description = "部门")
public class DeptCO extends TreeUtil.TreeNode<DeptCO> {

	@Serial
	private static final long serialVersionUID = 4116703987840123059L;

	@Schema(name = "sort", description = "部门排序")
	private Integer sort;

}
