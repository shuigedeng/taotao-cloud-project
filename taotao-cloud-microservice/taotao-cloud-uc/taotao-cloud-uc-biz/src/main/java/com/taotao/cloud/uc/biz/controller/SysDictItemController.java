package com.taotao.cloud.uc.biz.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.uc.api.dto.dictItem.DictItemDTO;
import com.taotao.cloud.uc.api.query.dictItem.DictItemPageQuery;
import com.taotao.cloud.uc.api.query.dictItem.DictItemQuery;
import com.taotao.cloud.uc.api.vo.dictItem.DictItemVO;
import com.taotao.cloud.uc.biz.entity.SysDictItem;
import com.taotao.cloud.uc.biz.service.ISysDictItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
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
 * 字典项管理API
 *
 * @author dengtao
 * @since 2020/4/30 11:21
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("/dict/item")
@Tag(name = "字典项管理API", description = "字典项管理API")
public class SysDictItemController {

	private final ISysDictItemService dictItemService;

	@Operation(summary = "添加字典项详情", description = "添加字典项详情", method = CommonConstant.POST, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "添加字典项详情")
	@PreAuthorize("hasAuthority('sys:dictItem:add')")
	@PostMapping
	public Result<Boolean> save(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "添加字典项详情DTO", required = true)
		@Validated @RequestBody DictItemDTO dictItemDTO) {
		SysDictItem item = dictItemService.save(dictItemDTO);
		return Result.success(Objects.nonNull(item));
	}

	@Operation(summary = "更新字典项详情", description = "更新字典项详情", method = CommonConstant.POST, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "更新字典项详情")
	@PreAuthorize("hasAuthority('sys:dictItem:edit')")
	@PutMapping("/{id}")
	public Result<Boolean> updateById(
		@Parameter(name = "id", description = "字典项id", required = true, in = ParameterIn.PATH)
		@PathVariable(value = "id") Long id,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "更新字典项详情DTO", required = true)
		@Validated @RequestBody DictItemDTO dictItemDTO) {
		SysDictItem item = dictItemService.updateById(id, dictItemDTO);
		return Result.success(Objects.nonNull(item));
	}

	@Operation(summary = "删除字典项详情", description = "删除字典项详情", method = CommonConstant.DELETE, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据id删除字典项详情")
	@PreAuthorize("hasAuthority('sys:dictItem:del')")
	@DeleteMapping("/{id:[0-9]*}")
	public Result<Boolean> deleteById(
		@Parameter(name = "id", description = "字典项id", required = true, in = ParameterIn.PATH)
		@PathVariable("id") Long id) {
		Boolean result = dictItemService.deleteById(id);
		return Result.success(result);
	}

	@Operation(summary = "分页查询字典详情", description = "分页查询字典详情", method = CommonConstant.GET, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "分页查询字典详情")
	@PreAuthorize("hasAuthority('sys:user:add')")
	@GetMapping("/page")
	public Result<PageModel<DictItemVO>> getPage(
		 DictItemPageQuery dictItemPageQuery) {
		Pageable pageable = PageRequest
			.of(dictItemPageQuery.getCurrentPage(), dictItemPageQuery.getPageSize());
		Page<SysDictItem> page = dictItemService.getPage(pageable, dictItemPageQuery);
		List<DictItemVO> collect = page.stream().filter(Objects::nonNull)
			.map(tuple -> {
				DictItemVO vo = DictItemVO.builder().build();
				BeanUtil.copyProperties(tuple, vo,
					CopyOptions.create().ignoreNullValue().ignoreError());
				return vo;
			}).collect(Collectors.toList());
		Page<DictItemVO> result = new PageImpl<>(collect, pageable, page.getTotalElements());
		return Result.success(PageModel.convertJpaPage(result));
	}

	@Operation(summary = "查询字典详情", description = "查询字典详情", method = "POST",
		security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "查询字典详情")
	@GetMapping("/info")
	public Result<List<DictItemVO>> getInfo(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "查询字典详情DTO")
			DictItemQuery dictItemQuery) {
		List<SysDictItem> items = dictItemService.getInfo(dictItemQuery);
		List<DictItemVO> collect = items.stream().filter(Objects::nonNull)
			.map(tuple -> {
				DictItemVO vo = DictItemVO.builder().build();
				BeanUtil.copyProperties(tuple, vo,
					CopyOptions.create().ignoreNullValue().ignoreError());
				return vo;
			}).collect(Collectors.toList());
		return Result.success(collect);
	}
}
