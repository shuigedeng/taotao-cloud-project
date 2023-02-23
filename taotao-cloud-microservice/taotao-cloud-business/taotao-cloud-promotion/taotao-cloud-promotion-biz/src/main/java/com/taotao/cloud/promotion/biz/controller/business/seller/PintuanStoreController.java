package com.taotao.cloud.promotion.biz.controller.business.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.PromotionTypeEnum;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.OperationalJudgment;
import com.taotao.cloud.web.request.annotation.RequestLogger;
import com.taotao.cloud.promotion.api.model.query.PintuanPageQuery;
import com.taotao.cloud.promotion.api.model.vo.PintuanVO;
import com.taotao.cloud.promotion.biz.model.entity.Pintuan;
import com.taotao.cloud.promotion.biz.model.entity.PromotionGoods;
import com.taotao.cloud.promotion.biz.service.business.IPintuanService;
import com.taotao.cloud.promotion.biz.service.business.IPromotionGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Collections;
import java.util.Objects;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
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
 * 店铺端,拼团管理接口
 *
 * @since 2020/10/9
 */
@RestController
@Tag(name = "店铺端,拼团管理接口")
@RequestMapping("/store/promotion/pintuan")
public class PintuanStoreController {

	@Autowired
	private IPintuanService pintuanService;
	@Autowired
	private IPromotionGoodsService promotionGoodsService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping
	@Operation(summary = "根据条件分页查询拼团活动列表")
	public Result<IPage<Pintuan>> getPintuanByPage(PintuanPageQuery queryParam, PageVO pageVo) {
		AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
		queryParam.setStoreId(currentUser.getStoreId());
		return Result.success(pintuanService.pageFindAll(queryParam, pageVo));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping(value = "/{id}")
	@Operation(summary = "通过id获取")
	public Result<PintuanVO> get(@PathVariable String id) {
		PintuanVO pintuan = OperationalJudgment.judgment(pintuanService.getPintuanVO(id));
		return Result.success(pintuan);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/goods/{pintuanId}")
	@Operation(summary = "根据条件分页查询拼团活动商品列表")
	public Result<IPage<PromotionGoods>> getPintuanGoodsByPage(@PathVariable String pintuanId,
		PageVO pageVo) {
		AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
		PromotionGoodsSearchParams searchParams = new PromotionGoodsSearchParams();
		searchParams.setStoreId(currentUser.getStoreId());
		searchParams.setPromotionId(pintuanId);
		searchParams.setPromotionType(PromotionTypeEnum.PINTUAN.name());
		return Result.success(promotionGoodsService.pageFindAll(searchParams, pageVo));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PostMapping(consumes = "application/json", produces = "application/json")
	@Operation(summary = "添加拼团活动")
	public Result<String> addPintuan(@RequestBody @Validated PintuanVO pintuan) {
		AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
		pintuan.setStoreId(currentUser.getStoreId());
		pintuan.setStoreName(currentUser.getStoreName());
		if (pintuanService.savePromotions(pintuan)) {
			return Result.success(ResultEnum.PINTUAN_ADD_SUCCESS);
		}
		throw new BusinessException(ResultEnum.PINTUAN_ADD_ERROR);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PutMapping(consumes = "application/json", produces = "application/json")
	@Operation(summary = "修改拼团活动")
	public Result<String> editPintuan(@RequestBody @Validated PintuanVO pintuan) {
		OperationalJudgment.judgment(pintuanService.getById(pintuan.getId()));
		AuthUser currentUser = Objects.requireNonNull(UserContext.getCurrentUser());
		pintuan.setStoreId(currentUser.getStoreId());
		pintuan.setStoreName(currentUser.getStoreName());
		if (pintuanService.updatePromotions(pintuan)) {
			return Result.success(ResultEnum.PINTUAN_EDIT_SUCCESS);
		}
		throw new BusinessException(ResultEnum.PINTUAN_EDIT_ERROR);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PutMapping("/status/{pintuanId}")
	@Operation(summary = "操作拼团活动状态")
	public Result<String> openPintuan(@PathVariable String pintuanId, Long startTime,
		Long endTime) {
		OperationalJudgment.judgment(pintuanService.getById(pintuanId));
		if (pintuanService.updateStatus(Collections.singletonList(pintuanId), startTime, endTime)) {
			return Result.success(ResultEnum.PINTUAN_MANUAL_OPEN_SUCCESS);
		}
		throw new BusinessException(ResultEnum.PINTUAN_MANUAL_OPEN_ERROR);

	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@DeleteMapping("/{pintuanId}")
	@Operation(summary = "手动删除拼团活动")
	public Result<String> deletePintuan(@PathVariable String pintuanId) {
		OperationalJudgment.judgment(pintuanService.getById(pintuanId));
		if (pintuanService.removePromotions(Collections.singletonList(pintuanId))) {
			return Result.success(ResultEnum.PINTUAN_DELETE_SUCCESS);
		}
		throw new BusinessException(ResultEnum.PINTUAN_DELETE_ERROR);
	}

}
