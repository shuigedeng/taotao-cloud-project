package com.taotao.cloud.goods.biz.controller.manager;


import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.biz.entity.GoodsUnit;
import com.taotao.cloud.goods.biz.service.GoodsUnitService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
 * 管理端,商品计量单位接口
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-商品计量单位管理API", description = "平台管理端-商品计量单位管理API")
@RequestMapping("/goods/manager/goods/unit")
public class GoodsUnitManagerController {

	/**
	 * 商品计量服务
	 */
	private final GoodsUnitService goodsUnitService;

	@Operation(summary = "分页获取商品计量单位", description = "分页获取商品计量单位")
	@RequestLogger("分页获取商品计量单位")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<PageModel<GoodsUnit>> getByPage(PageParam pageParam) {
		IPage<GoodsUnit> page = goodsUnitService.page(pageParam.buildMpPage());
		return Result.success(PageModel.convertMybatisPage(page, GoodsUnit.class));
	}

	@Operation(summary = "获取商品计量单位", description = "获取商品计量单位")
	@RequestLogger("获取商品计量单位")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/{id}")
	public Result<GoodsUnit> getById(@NotNull @PathVariable Long id) {
		return Result.success(goodsUnitService.getById(id));
	}

	@Operation(summary = "添加商品计量单位", description = "添加商品计量单位")
	@RequestLogger("添加商品计量单位")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(@Valid @RequestBody GoodsUnit goodsUnit) {
		return Result.success(goodsUnitService.save(goodsUnit));
	}

	@Operation(summary = "编辑商品计量单位", description = "编辑商品计量单位")
	@RequestLogger("编辑商品计量单位")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}")
	public Result<Boolean> update(@NotNull @PathVariable Long id, @Valid @RequestBody GoodsUnit goodsUnit) {
		goodsUnit.setId(Long.valueOf(id));
		return Result.success(goodsUnitService.updateById(goodsUnit));
	}

	@Operation(summary = "删除商品计量单位", description = "删除商品计量单位")
	@RequestLogger("删除商品计量单位")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping("/{ids}")
	public Result<Boolean> delete(@NotEmpty(message = "id不能为空") @PathVariable List<Long> ids) {
		return Result.success(goodsUnitService.removeByIds(ids));
	}

}
