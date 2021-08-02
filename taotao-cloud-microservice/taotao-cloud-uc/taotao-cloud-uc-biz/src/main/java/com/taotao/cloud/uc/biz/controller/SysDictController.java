package com.taotao.cloud.uc.biz.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.log.annotation.RequestOperateLog;
import com.taotao.cloud.uc.api.dto.dict.DictDTO;
import com.taotao.cloud.uc.api.query.dict.DictPageQuery;
import com.taotao.cloud.uc.api.vo.dict.DictVO;
import com.taotao.cloud.uc.biz.entity.SysDict;
import com.taotao.cloud.uc.biz.mapper.DictMapper;
import com.taotao.cloud.uc.biz.service.ISysDictService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
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
 * 字典管理API
 *
 * @author shuigedeng
 * @since 2020/4/30 11:13
 */
@Validated
@RestController
@RequestMapping("/dict")
@Tag(name = "字典管理API", description = "字典管理API")
public class SysDictController {

	private final ISysDictService dictService;

	public SysDictController(ISysDictService dictService) {
		this.dictService = dictService;
	}

	@Operation(summary = "添加字典信息", description = "添加字典信息", method = CommonConstant.POST, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "添加字典信息")
	@PreAuthorize("hasAuthority('sys:dict:save')")
	@PostMapping
	public Result<Boolean> save(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "添加字典对象DTO", required = true)
		@Validated @RequestBody DictDTO dictDTO) {
		SysDict dict = DictMapper.INSTANCE.dictDTOToSysDict(dictDTO);
		SysDict sysDict = dictService.save(dict);
		return Result.success(Objects.nonNull(sysDict));
	}

	@Operation(summary = "根据id更新字典信息", description = "根据id更新字典信息", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据id更新字典信息")
	@PreAuthorize("hasAuthority('sys:dict:update')")
	@PutMapping("/{id:[0-9]*}")
	public Result<Boolean> updateById(
		@Parameter(name = "id", description = "字典id", required = true, in = ParameterIn.PATH)
		@PathVariable(value = "id") Long id,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "更新字典对象DTO", required = true)
		@Validated @RequestBody DictDTO dictDTO) {
		SysDict dict = DictMapper.INSTANCE.dictDTOToSysDict(dictDTO);
		SysDict sysDict = dictService.update(dict);
		return Result.success(Objects.nonNull(sysDict));
	}

	@Operation(summary = "根据code更新字典信息", description = "根据code更新字典信息", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据code更新字典信息")
	@PreAuthorize("hasAuthority('sys:dict:update')")
	@PutMapping("/code/{code}")
	public Result<Boolean> updateByCode(
		@Parameter(name = "code", description = "字典code", required = true, in = ParameterIn.PATH)
		@PathVariable(value = "code") String code,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "更新字典对象DTO", required = true)
		@Validated @RequestBody DictDTO dictDTO) {
		SysDict dict = dictService.findByCode(code);
		BeanUtil
			.copyProperties(dictDTO, dict, CopyOptions.create().ignoreNullValue().ignoreError());
		SysDict sysDict = dictService.update(dict);
		return Result.success(Objects.nonNull(sysDict));
	}

	@Operation(summary = "查询所有字典集合", description = "查询所有字典集合", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "查询所有字典集合")
	@PreAuthorize("hasAuthority('sys:dipt:view')")
	@GetMapping
	public Result<List<DictVO>> getAll() {
		List<SysDict> sysDicts = dictService.getAll();
		List<DictVO> result = DictMapper.INSTANCE.sysDictToDictVO(sysDicts);
		return Result.success(result);
	}

	@Operation(summary = "分页查询字典集合", description = "分页查询字典集合", method = CommonConstant.PUT, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "分页查询字典集合")
	@PreAuthorize("hasAuthority('sys:dict:view')")
	@GetMapping("/page")
	public Result<PageModel<DictVO>> getPage(
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "分页查询字典集合DTO", required = true)
		@Validated DictPageQuery dictPageQuery) {
		Pageable pageable = PageRequest
			.of(dictPageQuery.getCurrentPage(), dictPageQuery.getPageSize());
		Page<SysDict> page = dictService.getPage(pageable, dictPageQuery);

		List<DictVO> collect = page.stream().filter(Objects::nonNull)
			.map(tuple -> {
				DictVO vo = DictVO.builder().build();
				BeanUtil.copyProperties(tuple, vo,
					CopyOptions.create().ignoreNullValue().ignoreError());
				return vo;
			}).collect(Collectors.toList());

		Page<DictVO> pageResult = new PageImpl<>(collect, pageable,
			page.getTotalElements());

		PageModel<DictVO> result = PageModel.convertJpaPage(pageResult);
		return Result.success(result);
	}

	@Operation(summary = "根据id删除字典", description = "根据id删除字典", method = CommonConstant.DELETE, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据id删除字典")
	@PreAuthorize("hasAuthority('sys:dict:del')")
	@DeleteMapping("/{id:[0-9]*}")
	public Result<Boolean> deleteById(
		@Parameter(name = "id", description = "字典id", required = true, in = ParameterIn.PATH)
		@PathVariable(value = "id") Long id) {
		Boolean result = dictService.removeById(id);
		return Result.success(result);
	}

	@Operation(summary = "根据code删除字典", description = "根据code删除字典", method = CommonConstant.DELETE, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据code删除字典")
	@PreAuthorize("hasAuthority('sys:dict:del')")
	@DeleteMapping("/code/{code}")
	public Result<Boolean> deleteByCode(
		@Parameter(name = "code", description = "字典code", required = true, in = ParameterIn.PATH)
		@PathVariable(value = "code") String code) {
		Boolean result = dictService.deleteByCode(code);
		return Result.success(result);
	}
}

