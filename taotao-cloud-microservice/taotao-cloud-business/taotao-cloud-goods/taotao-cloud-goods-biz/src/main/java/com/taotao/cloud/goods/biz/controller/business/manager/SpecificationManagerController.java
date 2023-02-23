package com.taotao.cloud.goods.biz.controller.business.manager;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.model.PageResult;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.model.dto.SpecificationDTO;
import com.taotao.cloud.goods.api.model.page.SpecificationPageQuery;
import com.taotao.cloud.goods.api.model.vo.SpecificationVO;
import com.taotao.cloud.goods.biz.model.convert.SpecificationConvert;
import com.taotao.cloud.goods.biz.model.entity.Specification;
import com.taotao.cloud.goods.biz.service.business.ISpecificationService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
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
 * 管理端,商品规格接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "平台管理端-商品规格管理API", description = "平台管理端-商品规格管理API")
@RequestMapping("/goods/manager/spec")
public class SpecificationManagerController {

	/**
	 * 商品规格服务
	 */
	private final ISpecificationService specificationService;

	@Operation(summary = "获取所有可用规格", description = "获取所有可用规格")
	@RequestLogger("获取所有可用规格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/all")
	public Result<List<SpecificationVO>> getAll() {
		List<Specification> specifications = specificationService.list();
		return Result.success(SpecificationConvert.INSTANCE.convert(specifications));
	}

	@Operation(summary = "搜索规格", description = "搜索规格")
	@RequestLogger("搜索规格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<PageResult<SpecificationVO>> page(SpecificationPageQuery specificationPageQuery) {
		IPage<Specification> specificationPage = specificationService.getPage(
				specificationPageQuery);
		return Result.success(
				PageResult.convertMybatisPage(specificationPage, SpecificationVO.class));
	}

	@Operation(summary = "保存规格", description = "保存规格")
	@RequestLogger("保存规格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(@Valid @RequestBody SpecificationDTO specificationDTO) {
		Specification specification = SpecificationConvert.INSTANCE.convert(
				specificationDTO);
		return Result.success(specificationService.save(specification));
	}

	@Operation(summary = "更改规格", description = "更改规格")
	@RequestLogger("更改规格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}")
	public Result<Boolean> update(@Valid @RequestBody SpecificationDTO specificationDTO,
			@PathVariable Long id) {
		Specification specification = SpecificationConvert.INSTANCE.convert(
				specificationDTO);
		specification.setId(id);

		return Result.success(specificationService.saveOrUpdate(specification));
	}

	@Operation(summary = "批量删除", description = "批量删除")
	@RequestLogger("批量删除")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping("/{ids}")
	public Result<Boolean> delAllByIds(@PathVariable List<Long> ids) {
		return Result.success(specificationService.deleteSpecification(ids));
	}
}
