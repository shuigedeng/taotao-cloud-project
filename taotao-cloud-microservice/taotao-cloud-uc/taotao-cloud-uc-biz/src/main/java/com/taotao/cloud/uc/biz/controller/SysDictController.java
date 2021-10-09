/*
 * Copyright 2002-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.uc.biz.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.LogUtil;
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
 * @version 2021.9
 * @since 2021-10-09 14:24:19
 */
@Validated
@RestController
@RequestMapping("/uc/dict")
@Tag(name = "字典管理API", description = "字典管理API")
public class SysDictController {

	private final ISysDictService dictService;

	public SysDictController(ISysDictService dictService) {
		this.dictService = dictService;
	}

	/**
	 * 添加字典信息
	 *
	 * @param dictDTO 添加字典对象DTO
	 * @return {@link Result&lt;java.lang.Boolean&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 14:50:19
	 */
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


	/**
	 * 根据id更新字典信息
	 *
	 * @param id      字典id
	 * @param dictDTO dictD更新字典对象DTOTO
	 * @return {@link Result&lt;java.lang.Boolean&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 14:53:49
	 */
	@Operation(summary = "根据id更新字典信息", description = "根据id更新字典信息", method = CommonConstant.PUT,
		security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据id更新字典信息")
	@PreAuthorize("hasAuthority('sys:dict:update')")
	@PutMapping("/{id:[0-9]*}")
	public Result<Boolean> updateById(
		@Parameter(name = "id", description = "字典id", required = true, in = ParameterIn.PATH)
		@PathVariable(value = "id") Long id,
		@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "更新字典对象DTO", required = true)
		@Validated @RequestBody DictDTO dictDTO) {
		LogUtil.info("xxxxx={}", id);
		SysDict dict = DictMapper.INSTANCE.dictDTOToSysDict(dictDTO);
		SysDict sysDict = dictService.update(dict);
		return Result.success(Objects.nonNull(sysDict));
	}

	/**
	 * 根据code更新字典信息
	 *
	 * @param code    字典code
	 * @param dictDTO 更新字典对象DTO
	 * @return {@link Result&lt;java.lang.Boolean&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 14:55:16
	 */
	@Operation(summary = "根据code更新字典信息", description = "根据code更新字典信息", method = CommonConstant.PUT,
		security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
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

	/**
	 * 查询所有字典集合
	 *
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.dict.DictVO&gt;&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 14:56:06
	 */
	@Operation(summary = "查询所有字典集合", description = "查询所有字典集合", method = CommonConstant.PUT,
		security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "查询所有字典集合")
	@PreAuthorize("hasAuthority('sys:dipt:view')")
	@GetMapping
	public Result<List<DictVO>> getAll() {
		List<SysDict> sysDicts = dictService.getAll();
		List<DictVO> result = DictMapper.INSTANCE.sysDictToDictVO(sysDicts);
		return Result.success(result);
	}

	/**
	 * 分页查询字典集合
	 *
	 * @param dictPageQuery 分页查询字典集合DTO
	 * @return {@link Result&lt;PageModel&lt;com.taotao.cloud.uc.api.vo.dict.DictVO&gt;&gt;}
	 * @author shuigedeng
	 * @since 2021-10-09 14:56:36
	 */
	@Operation(summary = "分页查询字典集合", description = "分页查询字典集合", method = CommonConstant.PUT,
		security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
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

	/**
	 * 根据id删除字典
	 *
	 * @param id 字典id
	 * @return {@link Result&lt;java.lang.Boolean&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 14:57:42
	 */
	@Operation(summary = "根据id删除字典", description = "根据id删除字典", method = CommonConstant.DELETE,
		security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	@RequestOperateLog(description = "根据id删除字典")
	@PreAuthorize("hasAuthority('sys:dict:del')")
	@DeleteMapping("/{id:[0-9]*}")
	public Result<Boolean> deleteById(
		@Parameter(name = "id", description = "字典id", required = true, in = ParameterIn.PATH)
		@PathVariable(value = "id") Long id) {
		Boolean result = dictService.removeById(id);
		return Result.success(result);
	}

	/**
	 * 根据code删除字典
	 *
	 * @param code 字典code
	 * @return {@link Result&lt;java.lang.Boolean&gt; }
	 * @author shuigedeng
	 * @since 2021-10-09 14:58:06
	 */
	@Operation(summary = "根据code删除字典", description = "根据code删除字典", method = CommonConstant.DELETE,
		security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
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

