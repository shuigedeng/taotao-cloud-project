package com.taotao.cloud.uc.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author shuigedeng
 * @since 2020/5/14 10:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "SysDeptTreeVo", description = "部门树VO")
public class SysDeptTreeVo {

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的key")
	private int key;

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的value")
	private String value;

	@Schema(description = "对应depart_name字段,前端数据树中的title")
	private String title;

	@Schema(description = "部门主键ID")
	private Integer deptId;

	@Schema(description = "部门名称")
	private String name;

	@Schema(description = "上级部门")
	private Integer parentId;

	@Schema(description = "排序")
	private Integer sort;

	@Schema(description = "备注")
	private String remark;

	@Schema(description = "创建时间")
	private LocalDateTime createTime;

	@Schema(description = "修改时间")
	private LocalDateTime updateTime;

	@Schema(description = "是否删除  -1：已删除  0：正常")
	private String delFlag;

	@Schema(description = "上级部门")
	private String parentName;

	@Schema(description = "等级")
	private Integer level;

	@Schema(description = "children")
	private List<SysDeptTreeVo> children;
}
