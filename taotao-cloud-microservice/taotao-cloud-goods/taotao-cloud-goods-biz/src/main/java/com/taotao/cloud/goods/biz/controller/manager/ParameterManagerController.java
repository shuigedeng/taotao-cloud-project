package com.taotao.cloud.goods.biz.controller.manager;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.biz.entity.Parameters;
import com.taotao.cloud.goods.biz.service.ParametersService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,分类绑定参数组管理接口
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-分类绑定参数组管理API", description = "平台管理端-分类绑定参数组管理API")
@RequestMapping("/goods/manager/parameters")
public class ParameterManagerController {

	/**
	 * 分类绑定参数服务
	 */
	private final ParametersService parametersService;

	@Operation(summary = "添加参数", description = "添加参数", method = CommonConstant.POST)
	@RequestLogger("添加参数添加参数")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(@Validated @RequestBody Parameters parameters) {
		return Result.success(parametersService.save(parameters));
	}

	@Operation(summary = "编辑参数", description = "编辑参数", method = CommonConstant.PUT)
	@RequestLogger("编辑参数")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping
	public Result<Boolean> update(@Validated @RequestBody Parameters parameters) {
		return Result.success(parametersService.updateParameter(parameters));
	}

	@Operation(summary = "根据id查询物流公司信息", description = "根据id查询物流公司信息", method = CommonConstant.DELETE)
	@RequestLogger("根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delById(@PathVariable Long id) {
		return Result.success(parametersService.removeById(id));
	}
}
