/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
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
package com.taotao.cloud.sys.biz.api.controller.manager;

import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.tree.ForestNodeMerger;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.security.annotation.NotAuth;
import com.taotao.cloud.sys.api.dto.region.RegionSaveDTO;
import com.taotao.cloud.sys.api.dto.region.RegionUpdateDTO;
import com.taotao.cloud.sys.api.vo.region.RegionParentVO;
import com.taotao.cloud.sys.api.vo.region.RegionQueryVO;
import com.taotao.cloud.sys.api.vo.region.RegionTreeVO;
import com.taotao.cloud.sys.biz.model.entity.region.Region;
import com.taotao.cloud.sys.biz.service.IRegionService;
import com.taotao.cloud.web.base.controller.SuperController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.constraints.NotNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 平台管理端-地区管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 15:01:59
 */
@RestController
@RequestMapping("/sys/manager/region")
@Tag(name = "平台管理端-地区管理API", description = "平台管理端-地区管理API")
public class ManagerRegionController extends
	SuperController<IRegionService, Region, Long, BaseQuery, RegionSaveDTO, RegionUpdateDTO, RegionQueryVO> {

	@Operation(summary = "根据父id查询地区数据", description = "根据父id查询地区数据")
	@RequestLogger
	@GetMapping("/parentId/{parentId}")
	@NotAuth
	//@PreAuthorize("hasAuthority('sys:region:info:parentId')")
	public Result<List<RegionParentVO>> queryRegionByParentId(
		@Parameter(description = "父id") @NotNull(message = "父id不能为空")
		@PathVariable(name = "parentId") Long parentId) {
		List<RegionParentVO> result = service().queryRegionByParentId(parentId);
		return Result.success(result);
	}

	@Operation(summary = "树形结构查询", description = "树形结构查询")
	@RequestLogger
	@GetMapping(value = "/tree")
	@NotAuth
	//@PreAuthorize("hasAuthority('sys:region:info:true')")
	public Result<List<RegionParentVO>> tree() {
		List<RegionParentVO> result = service().tree();
		return Result.success(result);
	}

	@Operation(summary = "另一种树形结构查询", description = "另一种树形结构查询")
	@RequestLogger
	@GetMapping(value = "/other/tree")
	@PreAuthorize("hasAuthority('sys:region:info:true')")
	public Result<List<RegionTreeVO>> treeOther() {
		List<RegionTreeVO> result = service().treeOther();
		return Result.success(ForestNodeMerger.merge(result));
	}

}
