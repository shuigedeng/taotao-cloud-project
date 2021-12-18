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
package com.taotao.cloud.sys.biz.controller.manager;

import com.taotao.cloud.common.model.BaseQuery;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.sys.api.dto.region.RegionSaveDTO;
import com.taotao.cloud.sys.api.dto.region.RegionUpdateDTO;
import com.taotao.cloud.sys.api.vo.region.RegionParentVO;
import com.taotao.cloud.sys.api.vo.region.RegionQueryVO;
import com.taotao.cloud.sys.biz.entity.Region;
import com.taotao.cloud.sys.biz.entity.SysRegion;
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
 * 地区管理API
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-10-09 15:01:59
 */
@RestController
@RequestMapping("/manager/region")
@Tag(name = "地区管理API", description = "地区管理API")
public class ManagerRegionController extends
	SuperController<IRegionService, Region, Long, BaseQuery, RegionSaveDTO, RegionUpdateDTO, RegionQueryVO> {


	/**
	 * 根据父id查询地区数据
	 *
	 * @param parentId 父id
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.sys.api.vo.region.QueryRegionByParentIdVO&gt;&gt;
	 * }
	 * @author shuigedeng
	 * @since 2021-10-14 11:30:36
	 */
	@Operation(summary = "根据父id查询地区数据", description = "根据父id查询地区数据")
	@RequestLogger(description = "根据父id查询")
	@GetMapping("/parentId/{parentId}")
	//@PreAuthorize("hasAuthority('sys:region:info:parentId')")
	public Result<List<RegionParentVO>> queryRegionByParentId(
		@Parameter(description = "父id") @NotNull(message ="父id不能为空")
		@PathVariable(name = "parentId") Long parentId) {
		List<RegionParentVO> result = service().queryRegionByParentId(parentId);
		return Result.success(result);
	}

	/**
	 * 树形结构查询
	 *
	 * @return {@link Result&lt;java.util.List&lt;com.taotao.cloud.sys.api.vo.region.QueryRegionByParentIdVO&gt;&gt;
	 * }
	 * @author shuigedeng
	 * @since 2021-10-14 11:32:28
	 */
	@Operation(summary = "树形结构查询", description = "树形结构查询")
	@RequestLogger(description = "根据父id查询")
	@GetMapping(value = "/tree")
	@PreAuthorize("hasAuthority('sys:region:info:true')")
	public Result<List<RegionParentVO>> tree() {
		List<RegionParentVO> result = service().tree();
		return Result.success(result);
	}
}
