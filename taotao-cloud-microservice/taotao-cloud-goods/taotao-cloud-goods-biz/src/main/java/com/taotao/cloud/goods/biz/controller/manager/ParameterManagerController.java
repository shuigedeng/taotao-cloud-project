package com.taotao.cloud.goods.biz.controller.manager;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.dto.ParametersDTO;
import com.taotao.cloud.goods.biz.entity.Parameters;
import com.taotao.cloud.goods.biz.mapstruct.IParametersMapStruct;
import com.taotao.cloud.goods.biz.service.IParametersService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-参数管理API", description = "平台管理端-参数管理API")
@RequestMapping("/goods/manager/parameters")
public class ParameterManagerController {

	/**
	 * 参数服务
	 */
	private final IParametersService parametersService;

	@Operation(summary = "添加参数", description = "添加参数")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(@Validated @RequestBody ParametersDTO parametersDTO) {
		Parameters parameters = IParametersMapStruct.INSTANCE.parametersDTOToParameters(
			parametersDTO);
		return Result.success(parametersService.save(parameters));
	}

	@Operation(summary = "编辑参数", description = "编辑参数")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}")
	public Result<Boolean> update(@Validated @RequestBody ParametersDTO parametersDTO,
		@PathVariable Long id) {
		Parameters parameters = IParametersMapStruct.INSTANCE.parametersDTOToParameters(parametersDTO);
		parameters.setId(id);
		return Result.success(parametersService.updateParameter(parameters));
	}

	@Operation(summary = "根据id删除参数", description = "根据id删除参数")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> delById(@PathVariable Long id) {
		return Result.success(parametersService.removeById(id));
	}
}
