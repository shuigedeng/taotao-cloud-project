package com.taotao.cloud.goods.biz.controller.manager;


import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.PageParam;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.bean.BeanUtil;
import com.taotao.cloud.disruptor.util.StringUtils;
import com.taotao.cloud.goods.api.vo.SpecificationVO;
import com.taotao.cloud.goods.biz.entity.Specification;
import com.taotao.cloud.goods.biz.service.SpecificationService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
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
 * 管理端,商品规格接口
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
	private final SpecificationService specificationService;

	@Operation(summary = "获取所有可用规格", description = "获取所有可用规格", method = CommonConstant.GET)
	@RequestLogger("获取所有可用规格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/all")
	public Result<List<SpecificationVO>> getAll() {
		List<Specification> list = specificationService.list();
		return Result.success(BeanUtil.copy(list, SpecificationVO.class));
	}

	@Operation(summary = "搜索规格", description = "搜索规格", method = CommonConstant.GET)
	@RequestLogger("搜索规格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<PageModel<SpecificationVO>> page(String specName, PageParam page) {
		return Result.success(specificationService.getPage(specName, page));
	}

	@Operation(summary = "保存规格", description = "保存规格", method = CommonConstant.POST)
	@RequestLogger("保存规格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> save(@Valid @RequestBody Specification specification) {
		return Result.success(specificationService.save(specification));
	}

	@Operation(summary = "更改规格", description = "更改规格", method = CommonConstant.PUT)
	@RequestLogger("更改规格")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PutMapping("/{id}")
	public Result<Boolean> update(@Valid @RequestBody Specification specification,
		@PathVariable Long id) {
		specification.setId(id);
		return Result.success(specificationService.saveOrUpdate(specification));
	}

	@Operation(summary = "批量删除", description = "批量删除", method = CommonConstant.DELETE)
	@RequestLogger("批量删除")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping("/{ids}")
	public Result<Boolean> delAllByIds(@PathVariable List<Long> ids) {
		return Result.success(specificationService.deleteSpecification(ids));
	}
}
