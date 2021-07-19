package com.taotao.cloud.uc.api.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 构建部门树vo
 *
 * @author shuigedeng
 * @since 2020/5/2 16:42
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "DeptTreeVo", description = "部门树VO")
public class DeptTreeVo {

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的key")
	private Long key;

	@Schema(description = "对应SysDepart中的id字段,前端数据树中的value")
	private String value;

	@Schema(description = "对应depart_name字段,前端数据树中的title")
	private String title;

	@Schema(description = "部门树children")
	private List<DeptTreeVo> children;

}
