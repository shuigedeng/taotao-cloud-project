package com.taotao.cloud.sys.api.vo.dept;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Schema(description = "部门树VO")
public record SysDeptTreeVo(
	@Schema(description = "对应SysDepart中的id字段,前端数据树中的key")
	int key,

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的value")
	String value,

	@Schema(description = "对应depart_name字段,前端数据树中的title")
	String title,

	@Schema(description = "部门主键ID")
	Integer deptId,

	@Schema(description = "部门名称")
	String name,

	@Schema(description = "上级部门")
	Integer parentId,

	@Schema(description = "排序")
	Integer sort,

	@Schema(description = "备注")
	String remark,

	@Schema(description = "创建时间")
	LocalDateTime createTime,

	@Schema(description = "修改时间")
	LocalDateTime updateTime,

	@Schema(description = "是否删除  -1：已删除  0：正常")
	String delFlag,

	@Schema(description = "上级部门")
	String parentName,

	@Schema(description = "等级")
	Integer level,

	@Schema(description = "children")
	List<SysDeptTreeVo> children
) implements Serializable {

	@Serial
	static final long serialVersionUID = -4546704465269983480L;


}
