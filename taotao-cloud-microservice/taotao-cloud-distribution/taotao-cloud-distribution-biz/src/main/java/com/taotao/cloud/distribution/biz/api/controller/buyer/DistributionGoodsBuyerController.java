package com.taotao.cloud.distribution.biz.api.controller.buyer;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.distribution.api.web.query.DistributionGoodsPageQuery;
import com.taotao.cloud.distribution.api.web.vo.DistributionGoodsVO;
import com.taotao.cloud.distribution.biz.service.DistributionGoodsService;
import com.taotao.cloud.distribution.biz.service.DistributionSelectedGoodsService;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 买家端,分销商品接口
 */
@Validated
@RestController
@Tag(name = "买家端-分销商品接口", description = "买家端-分销商品接口")
@RequestMapping("/buyer/distribution/goods")
public class DistributionGoodsBuyerController {

	/**
	 * 分销商品
	 */
	@Autowired
	private DistributionGoodsService distributionGoodsService;
	/**
	 * 选择分销商品
	 */
	@Autowired
	private DistributionSelectedGoodsService distributionSelectedGoodsService;

	@Operation(summary = "获取分销商商品列表", description = "获取分销商商品列表")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping
	public Result<IPage<DistributionGoodsVO>> distributionGoods(
			DistributionGoodsPageQuery distributionGoodsPageQuery) {
		return Result.success(distributionGoodsService.goodsPage(distributionGoodsPageQuery));
	}

	@Operation(summary = "选择分销商品", description = "选择分销商品")
	@RequestLogger
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping("/tree")
	@PreventDuplicateSubmissions
	@GetMapping(value = "/checked/{distributionGoodsId}")
	public Result<Object> distributionCheckGoods(
			@NotNull(message = "分销商品不能为空") @PathVariable("distributionGoodsId") String distributionGoodsId,
			@Parameter(description = "是否选择") Boolean checked) {
		boolean result = false;
		if (checked) {
			result = distributionSelectedGoodsService.add(distributionGoodsId);
		} else {
			result = distributionSelectedGoodsService.delete(distributionGoodsId);
		}

		//判断操作结果
		if (result) {
			return Result.success(ResultEnum.SUCCESS);
		} else {
			throw new BusinessException(ResultEnum.ERROR);
		}

	}
}
