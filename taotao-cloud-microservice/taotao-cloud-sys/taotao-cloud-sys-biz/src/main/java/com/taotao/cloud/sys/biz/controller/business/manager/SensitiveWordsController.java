package com.taotao.cloud.sys.biz.controller.business.manager;

import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.sys.biz.model.entity.sensitive.SensitiveWord;
import com.taotao.cloud.sys.biz.service.business.ISensitiveWordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,敏感词管理接口
 */
@AllArgsConstructor
@Validated
@RestController
@RequestMapping("/sys/manager/sensitive/word")
@Tag(name = "平台管理端-敏感词管理API", description = "平台管理端-敏感词管理API")
public class SensitiveWordsController {

	private final ISensitiveWordService sensitiveWordService;

	@Operation(summary = "通过id获取", description = "通过id获取")
	@GetMapping(value = "/{id}")
	public Result<SensitiveWord> get(
		@Parameter(description = "敏感词ID", required = true) @NotBlank(message = "敏感词ID不能为空")
		@PathVariable String id) {
		return Result.success(sensitiveWordService.getById(id));
	}

	//@ApiOperation(value = "分页获取")
	//@GetMapping
	//public Result<IPage<SensitiveWord>> getByPage(PageVO page) {
	//	return Result.success(sensitiveWordService.page(PageUtil.initPage(page)));
	//}

	@Operation(summary = "添加敏感词", description = "添加敏感词")
	@PostMapping
	public Result<SensitiveWord> add(@Valid @RequestBody SensitiveWord sensitiveWords) {
		sensitiveWordService.save(sensitiveWords);
		sensitiveWordService.resetCache();
		return Result.success(sensitiveWords);
	}

	@Operation(summary = "修改敏感词", description = "修改敏感词")
	@PutMapping("/{id}")
	public Result<SensitiveWord> edit(
		@Parameter(description = "敏感词ID", required = true) @NotNull(message = "敏感词ID不能为空")
		@PathVariable Long id, @RequestBody SensitiveWord sensitiveWords) {
		sensitiveWords.setId(id);
		sensitiveWordService.updateById(sensitiveWords);
		sensitiveWordService.resetCache();
		return Result.success(sensitiveWords);
	}

	@Operation(summary = "批量删除", description = "批量删除")
	@DeleteMapping(value = "/{ids}")
	public Result<Boolean> delAllByIds(
		@Parameter(description = "敏感词ID", required = true) @NotEmpty(message = "敏感词ID不能为空")
		@PathVariable List<String> ids) {
		sensitiveWordService.removeByIds(ids);
		sensitiveWordService.resetCache();
		return Result.success(true);
	}
}
