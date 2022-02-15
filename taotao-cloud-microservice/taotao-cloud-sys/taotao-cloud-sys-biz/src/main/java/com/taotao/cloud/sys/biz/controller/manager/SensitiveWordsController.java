package com.taotao.cloud.sys.biz.controller.manager;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.biz.entity.SensitiveWord;
import com.taotao.cloud.sys.biz.service.ISensitiveWordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.apache.pulsar.shade.io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,敏感词管理接口
 */
@Validated
@RestController
@RequestMapping("/sys/manager/sensitive/word")
@Tag(name = "平台管理端-敏感词管理API", description = "平台管理端-敏感词管理API")
public class SensitiveWordsController {

	@Autowired
	private ISensitiveWordService ISensitiveWordService;

	@Operation(summary = "通过id获取", description = "通过id获取")
	@GetMapping(value = "/get/{id}")
	public Result<SensitiveWord> get(
		@Parameter(description = "敏感词ID", required = true) @NotNull(message = "敏感词ID不能为空")
		@PathVariable String id) {
		return Result.success(ISensitiveWordService.getById(id));
	}

	//@ApiOperation(value = "分页获取")
	//@GetMapping
	//public Result<IPage<SensitiveWord>> getByPage(PageVO page) {
	//	return ResultUtil.data(sensitiveWordService.page(PageUtil.initPage(page)));
	//}

	@Operation(summary = "添加敏感词", description = "添加敏感词")
	@PostMapping
	public Result<SensitiveWord> add(@Valid SensitiveWord sensitiveWords) {
		ISensitiveWordService.save(sensitiveWords);
		ISensitiveWordService.resetCache();

		return Result.success(sensitiveWords);
	}

	@Operation(summary = "修改敏感词", description = "修改敏感词")
	@PutMapping("/{id}")
	public Result<SensitiveWord> edit(
		@Parameter(description = "敏感词ID", required = true) @NotNull(message = "敏感词ID不能为空")
		@PathVariable Long id, SensitiveWord sensitiveWords) {
		sensitiveWords.setId(id);
		ISensitiveWordService.updateById(sensitiveWords);
		ISensitiveWordService.resetCache();

		return Result.success(sensitiveWords);
	}

	@Operation(summary = "批量删除", description = "批量删除")
	@ApiImplicitParam(name = "ids", value = "敏感词ID", required = true, dataType = "String", allowMultiple = true, paramType = "path")
	@DeleteMapping(value = "/delByIds/{ids}")
	public Result<Boolean> delAllByIds(
		@Parameter(description = "敏感词ID", required = true) @NotEmpty(message = "敏感词ID不能为空")
		@PathVariable List<String> ids) {
		ISensitiveWordService.removeByIds(ids);
		ISensitiveWordService.resetCache();

		return Result.success(true);
	}
}
