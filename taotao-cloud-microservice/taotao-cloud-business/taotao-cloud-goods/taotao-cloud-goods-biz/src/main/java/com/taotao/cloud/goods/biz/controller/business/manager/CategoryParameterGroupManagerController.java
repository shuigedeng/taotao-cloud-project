package com.taotao.cloud.goods.biz.controller.business.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.model.vo.ParameterGroupVO;
import com.taotao.cloud.goods.biz.model.entity.CategoryParameterGroup;
import com.taotao.cloud.goods.biz.model.entity.Parameters;
import com.taotao.cloud.goods.biz.service.business.ICategoryParameterGroupService;
import com.taotao.cloud.goods.biz.service.business.IParametersService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.AllArgsConstructor;
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
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "管理端-分类绑定参数组API", description = "管理端-分类绑定参数组API")
@RequestMapping("/goods/manager/category/parameters")
public class CategoryParameterGroupManagerController {

	/**
	 * 商品参数组服务
	 */
	private final IParametersService parametersService;
	/**
	 * 分类绑定参数组服务
	 */
	private final ICategoryParameterGroupService categoryParameterGroupService;

	@Operation(summary = "查询某分类下绑定的参数信息", description = "查询某分类下绑定的参数信息")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{categoryId}")
	public Result<List<ParameterGroupVO>> getCategoryParam(@PathVariable Long categoryId) {
		return Result.success(categoryParameterGroupService.getCategoryParams(categoryId));
	}

	@Operation(summary = "保存数据", description = "保存数据")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> saveOrUpdate(
		@Validated CategoryParameterGroup categoryParameterGroup) {
		return Result.success(categoryParameterGroupService.save(categoryParameterGroup));
	}

	@Operation(summary = "更新数据", description = "更新数据")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping
	public Result<Boolean> update(
		@Validated CategoryParameterGroup categoryParameterGroup) {
		return Result.success(categoryParameterGroupService.updateById(categoryParameterGroup));
	}

	@Operation(summary = "通过id删除参数组", description = "通过id删除参数组")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delAllByIds(@PathVariable Long id) {
		//删除参数
		parametersService.remove(new QueryWrapper<Parameters>().eq("group_id", id));
		//删除参数组
		return Result.success(categoryParameterGroupService.removeById(id));
	}

}
