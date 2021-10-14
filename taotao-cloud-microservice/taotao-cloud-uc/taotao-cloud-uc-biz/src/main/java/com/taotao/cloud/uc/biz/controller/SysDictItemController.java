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

import com.taotao.cloud.uc.api.dto.dict_item.DictItemQueryDTO;
import com.taotao.cloud.uc.api.dto.dict_item.DictItemSaveDTO;
import com.taotao.cloud.uc.api.dto.dict_item.DictItemUpdateDTO;
import com.taotao.cloud.uc.api.service.ISysDictItemService;
import com.taotao.cloud.uc.api.vo.dict_item.DictItemQueryVO;
import com.taotao.cloud.uc.biz.entity.SysDictItem;
import com.taotao.cloud.web.base.controller.SuperController;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 字典项管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 14:58:55
 */
@Validated
@RestController
@RequestMapping("/uc/dict/item")
@Tag(name = "字典项管理API", description = "字典项管理API")
public class SysDictItemController extends
	SuperController<ISysDictItemService<SysDictItem, Long>, SysDictItem, Long, DictItemQueryDTO, DictItemSaveDTO, DictItemUpdateDTO, DictItemQueryVO> {

	//private final ISysDictItemService dictItemService;
	//
	//public SysDictItemController(ISysDictItemService dictItemService) {
	//	this.dictItemService = dictItemService;
	//}
	//
	///**
	// * 添加字典项详情
	// *
	// * @param dictItemDTO 添加字典项详情DTO
	// * @return {@link Result&lt;java.lang.Boolean&gt; }
	// * @author shuigedeng
	// * @since 2021-10-09 14:59:07
	// */
	//@Operation(summary = "添加字典项详情", description = "添加字典项详情", method = CommonConstant.POST,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "添加字典项详情")
	//@PreAuthorize("hasAuthority('sys:dictItem:add')")
	//@PostMapping
	//public Result<Boolean> save(
	//	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "添加字典项详情DTO", required = true)
	//	@Validated @RequestBody DictItemDTO dictItemDTO) {
	//	SysDictItem item = dictItemService.save(dictItemDTO);
	//	return Result.success(Objects.nonNull(item));
	//}
	//
	///**
	// * 更新字典项详情
	// *
	// * @param id          字典项id
	// * @param dictItemDTO 更新字典项详情DTO
	// * @return {@link Result&lt;java.lang.Boolean&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:00:19
	// */
	//@Operation(summary = "更新字典项详情", description = "更新字典项详情", method = CommonConstant.POST,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "更新字典项详情")
	//@PreAuthorize("hasAuthority('sys:dictItem:edit')")
	//@PutMapping("/{id}")
	//public Result<Boolean> updateById(
	//	@Parameter(name = "id", description = "字典项id", required = true, in = ParameterIn.PATH)
	//	@PathVariable(value = "id") Long id,
	//	@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "更新字典项详情DTO", required = true)
	//	@Validated @RequestBody DictItemDTO dictItemDTO) {
	//	SysDictItem item = dictItemService.updateById(id, dictItemDTO);
	//	return Result.success(Objects.nonNull(item));
	//}
	//
	///**
	// * 删除字典项详情
	// *
	// * @param id 字典项id
	// * @return {@link Result&lt;java.lang.Boolean&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:00:40
	// */
	//@Operation(summary = "删除字典项详情", description = "删除字典项详情", method = CommonConstant.DELETE, security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "根据id删除字典项详情")
	//@PreAuthorize("hasAuthority('sys:dictItem:del')")
	//@DeleteMapping("/{id:[0-9]*}")
	//public Result<Boolean> deleteById(
	//	@Parameter(name = "id", description = "字典项id", required = true, in = ParameterIn.PATH)
	//	@PathVariable("id") Long id) {
	//	Boolean result = dictItemService.deleteById(id);
	//	return Result.success(result);
	//}
	//
	///**
	// * 分页查询字典详情
	// *
	// * @param dictItemPageQuery 查询对象
	// * @return {@link Result&lt;PageModel&lt;com.taotao.cloud.uc.api.vo.dictItem.DictItemVO&gt;&gt;}
	// * @author shuigedeng
	// * @since 2021-10-09 15:00:50
	// */
	//@Operation(summary = "分页查询字典详情", description = "分页查询字典详情", method = CommonConstant.GET,
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "分页查询字典详情")
	//@PreAuthorize("hasAuthority('sys:user:add')")
	//@GetMapping("/page")
	//public Result<PageModel<DictItemVO>> getPage(DictItemPageQuery dictItemPageQuery) {
	//	Pageable pageable = PageRequest
	//		.of(dictItemPageQuery.getCurrentPage(), dictItemPageQuery.getPageSize());
	//	Page<SysDictItem> page = dictItemService.getPage(pageable, dictItemPageQuery);
	//	List<DictItemVO> collect = page.stream().filter(Objects::nonNull)
	//		.map(tuple -> {
	//			DictItemVO vo = DictItemVO.builder().build();
	//			BeanUtil.copyProperties(tuple, vo,
	//				CopyOptions.create().ignoreNullValue().ignoreError());
	//			return vo;
	//		}).collect(Collectors.toList());
	//	Page<DictItemVO> result = new PageImpl<>(collect, pageable, page.getTotalElements());
	//	return Result.success(PageModel.convertJpaPage(result));
	//}
	//
	///**
	// * 查询字典详情
	// *
	// * @param dictItemQuery 查询对象
	// * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.uc.api.vo.dictItem.DictItemVO&gt;&gt;
	// * }
	// * @author shuigedeng
	// * @since 2021-10-09 15:01:07
	// */
	//@Operation(summary = "查询字典详情", description = "查询字典详情", method = "POST",
	//	security = @SecurityRequirement(name = HttpHeaders.AUTHORIZATION))
	//@RequestOperateLog(description = "查询字典详情")
	//@GetMapping("/info")
	//public Result<List<DictItemVO>> getInfo(DictItemQuery dictItemQuery) {
	//	List<SysDictItem> items = dictItemService.getInfo(dictItemQuery);
	//	List<DictItemVO> collect = items.stream().filter(Objects::nonNull)
	//		.map(tuple -> {
	//			DictItemVO vo = DictItemVO.builder().build();
	//			BeanUtil.copyProperties(tuple, vo,
	//				CopyOptions.create().ignoreNullValue().ignoreError());
	//			return vo;
	//		}).collect(Collectors.toList());
	//	return Result.success(collect);
	//}
}
