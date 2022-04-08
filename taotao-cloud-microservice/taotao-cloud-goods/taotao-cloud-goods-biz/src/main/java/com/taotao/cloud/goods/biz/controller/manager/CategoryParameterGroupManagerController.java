package com.taotao.cloud.goods.biz.controller.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.vo.ParameterGroupVO;
import com.taotao.cloud.goods.biz.entity.CategoryParameterGroup;
import com.taotao.cloud.goods.biz.entity.Parameters;
import com.taotao.cloud.goods.biz.service.CategoryParameterGroupService;
import com.taotao.cloud.goods.biz.service.ParametersService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,分类绑定参数组接口
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-分类绑定参数组API", description = "平台管理端-分类绑定参数组API")
@RequestMapping("/goods/manager/categoryParameters")
public class CategoryParameterGroupManagerController {

	/**
	 * 商品参数组服务
	 */
	private final ParametersService parametersService;
	/**
	 * 分类绑定参数组服务
	 */
	private final CategoryParameterGroupService categoryParameterGroupService;

	@Operation(summary = "查询某分类下绑定的参数信息", description = "查询某分类下绑定的参数信息", method = CommonConstant.GET)
	@RequestLogger("查询某分类下绑定的参数信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}")
	public Result<List<ParameterGroupVO>> getCategoryParam(@PathVariable Long categoryId) {
		return Result.success(categoryParameterGroupService.getCategoryParams(categoryId));
	}

	@Operation(summary = "保存数据", description = "保存数据", method = CommonConstant.POST)
	@RequestLogger("保存数据")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> saveOrUpdate(
		@Validated CategoryParameterGroup categoryParameterGroup) {
			return Result.success(categoryParameterGroupService.save(categoryParameterGroup));
	}

	@Operation(summary = "更新数据", description = "更新数据", method = CommonConstant.PUT)
	@RequestLogger("更新数据")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping
	public Result<Boolean> update(
		@Validated CategoryParameterGroup categoryParameterGroup) {
			return Result.success(categoryParameterGroupService.updateById(categoryParameterGroup));
	}

	@Operation(summary = "通过id删除参数组", description = "通过id删除参数组", method = CommonConstant.DELETE)
	@RequestLogger("通过id删除参数组")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delAllByIds(@PathVariable Long id) {
		//删除参数
		parametersService.remove(new QueryWrapper<Parameters>().eq("group_id", id));
		//删除参数组
		return Result.success(categoryParameterGroupService.removeById(id));
	}

}
