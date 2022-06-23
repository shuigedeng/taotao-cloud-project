package com.taotao.cloud.sys.api.web.vo.dept;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.taotao.cloud.common.tree.INode;
import com.taotao.cloud.common.tree.MapperNode;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.*;

/**
 * DepartVO
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-12-22 20:59:37
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Schema(description = "部门树VO")
public class DeptTreeVO extends MapperNode implements INode {

	@Serial
	private static final long serialVersionUID = -4132785717179910025L;

	///**
	// * 主键ID
	// */
	//private Long id;

	///**
	// * 父节点ID
	// */
	//private Long parentId;

	///**
	// * 子孙节点
	// */
	//private List<INode> children;

	///**
	// * 是否有子孙节点
	// */
	//private Boolean hasChildren;

	/**
	 * 部门名称
	 */
	private String name;
	///**
	// * 排序
	// */
	//private Integer sort;
	/**
	 * 删除标识
	 */
	private String isDeleted;
	/**
	 * 租户ID
	 */
	private Long tenantId;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "修改时间")
	private LocalDateTime updateTime;

	//@Override
	//public List<INode> getChildren() {
	//	if (this.children == null) {
	//		this.children = new ArrayList<>();
	//	}
	//	return this.children;
	//}

	@Override
	public <T extends INode> List<T> getChildren() {
		return super.getChildren();
	}
}
