package com.taotao.cloud.goods.biz.controller.manager;

import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.biz.entity.Parameters;
import com.taotao.cloud.goods.biz.service.ParametersService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@Validated
@RestController
@Tag(name = "平台管理端-分类绑定参数组管理API", description = "平台管理端-分类绑定参数组管理API")
@RequestMapping("/goods/manager/parameters")
public class ParameterManagerController {

	@Autowired
	private ParametersService parametersService;

	@Operation(summary = "添加参数", description = "添加参数", method = CommonConstant.POST)
	@RequestLogger(description = "添加参数添加参数")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(@Valid @RequestBody Parameters parameters) {
		return Result.success(parametersService.save(parameters));
	}

	@Operation(summary = "编辑参数", description = "编辑参数", method = CommonConstant.PUT)
	@RequestLogger(description = "编辑参数")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping
	public Result<Boolean> update(@Valid @RequestBody Parameters parameters) {
		return Result.success(parametersService.updateParameter(parameters));
	}

	@Operation(summary = "根据id查询物流公司信息", description = "根据id查询物流公司信息", method = CommonConstant.DELETE)
	@RequestLogger(description = "根据id查询物流公司信息")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delById(@PathVariable String id) {
		return Result.success(parametersService.removeById(id));
	}
}
