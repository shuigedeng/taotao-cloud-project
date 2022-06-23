package com.taotao.cloud.promotion.biz.api.controller.manager;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.promotion.api.web.query.PintuanPageQuery;
import com.taotao.cloud.promotion.api.web.vo.PintuanVO;
import com.taotao.cloud.promotion.api.web.query.PromotionGoodsPageQuery;
import com.taotao.cloud.promotion.biz.model.entity.Pintuan;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.PintuanService;
import com.taotao.cloud.promotion.biz.service.PromotionGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端,平台拼团接口
 *
 * @since 2020/10/9
 */
@RestController
@Tag(name = "管理端,平台拼团接口")
@RequestMapping("/manager/promotion/pintuan")
public class PintuanManagerController {

	@Autowired
	private PintuanService pintuanService;
	@Autowired
	private PromotionGoodsService promotionGoodsService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping(value = "/{id}")
	@Operation(summary = "通过id获取")
	public Result<PintuanVO> get(@PathVariable String id) {
		PintuanVO pintuan = pintuanService.getPintuanVO(id);
		return Result.success(pintuan);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping
	@Operation(summary = "根据条件分页查询拼团活动列表")
	public Result<IPage<Pintuan>> getPintuanByPage(PintuanPageQuery queryParam, PageVO pageVo) {
		IPage<Pintuan> pintuanIPage = pintuanService.pageFindAll(queryParam, pageVo);
		return Result.success(pintuanIPage);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/goods/{pintuanId}")
	@Operation(summary = "根据条件分页查询拼团活动商品列表")
	public Result<IPage<PromotionGoods>> getPintuanGoodsByPage(@PathVariable String pintuanId,
		PageVO pageVo) {
		PromotionGoodsPageQuery searchParams = new PromotionGoodsPageQuery();
		searchParams.setPromotionId(pintuanId);
		searchParams.setPromotionType(PromotionTypeEnum.PINTUAN.name());
		return Result.success(promotionGoodsService.pageFindAll(searchParams, pageVo));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PutMapping("/status/{pintuanIds}")
	@Operation(summary = "操作拼团活动状态")
	public Result<String> openPintuan(@PathVariable String pintuanIds, Long startTime,
		Long endTime) {
		if (pintuanService.updateStatus(Arrays.asList(pintuanIds.split(",")), startTime, endTime)) {
			return Result.success(ResultEnum.PINTUAN_MANUAL_OPEN_SUCCESS);
		}
		throw new BusinessException(ResultEnum.PINTUAN_MANUAL_OPEN_ERROR);

	}

}
