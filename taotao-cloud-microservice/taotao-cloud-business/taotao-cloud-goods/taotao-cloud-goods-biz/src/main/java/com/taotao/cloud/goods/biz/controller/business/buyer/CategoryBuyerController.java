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

package com.taotao.cloud.goods.biz.controller.business.buyer;

import com.taotao.boot.common.model.result.Result;
import com.taotao.cloud.goods.biz.model.vo.CategoryTreeVO;
import com.taotao.cloud.goods.biz.service.business.ICategoryService;
import com.taotao.boot.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 买家端,商品分类接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "买家端-商品分类API", description = "买家端-商品分类API")
@RequestMapping("/goods/buyer/category")
public class CategoryBuyerController {

	/**
	 * 商品分类
	 */
	private final ICategoryService categoryService;

	@RequestLogger
	@Operation(summary = "根据父id获取商品分类列表", description = "根据父id获取商品分类列表")
	@Parameters({
		@Parameter(name = "parentId", required = true, description = "父ID 0-最上级id", in = ParameterIn.PATH),
	})
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{parentId}")
	public Result<List<CategoryTreeVO>> list(@NotNull(message = "父ID不能为空") @PathVariable Long parentId) {
		return Result.success(categoryService.listAllChildren(parentId));
	}
}
