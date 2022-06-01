package com.taotao.cloud.promotion.biz.controller.manager;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.logger.annotation.RequestLogger;
import com.taotao.cloud.promotion.api.dto.KanjiaActivityGoodsDTO;
import com.taotao.cloud.promotion.api.dto.KanjiaActivityGoodsOperationDTO;
import com.taotao.cloud.promotion.api.vo.kanjia.KanjiaActivityGoodsParams;
import com.taotao.cloud.promotion.biz.entity.KanjiaActivityGoods;
import com.taotao.cloud.promotion.biz.service.KanjiaActivityGoodsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 管理端,促销接口
 */
@RestController
@Tag(name = "管理端,砍价促销接口")
@RequestMapping("/manager/promotion/kan-jia-goods")
public class KanJiaActivityGoodsManagerController {

	@Autowired
	private KanjiaActivityGoodsService kanJiaActivityGoodsService;

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PostMapping
	@Operation(summary = "添加砍价活动")
	public Result<Object> add(
		@RequestBody KanjiaActivityGoodsOperationDTO kanJiaActivityGoodsOperationDTO) {
		kanJiaActivityGoodsService.add(kanJiaActivityGoodsOperationDTO);
		return Result.success();
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@Operation(summary = "获取砍价活动分页")
	@GetMapping
	public Result<IPage<KanjiaActivityGoods>> getKanJiaActivityPage(
		KanjiaActivityGoodsParams KanJiaActivityParams, PageVO page) {
		return Result.success(kanJiaActivityGoodsService.getForPage(KanJiaActivityParams, page));
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@GetMapping("/{id}")
	@Operation(summary = "获取积分商品详情")
	public Result<KanjiaActivityGoodsDTO> getPointsGoodsDetail(@PathVariable("id") String goodsId) {
		KanjiaActivityGoodsDTO kanJiaActivityGoodsDTO = kanJiaActivityGoodsService.getKanjiaGoodsDetail(
			goodsId);
		return Result.success(kanJiaActivityGoodsDTO);
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@PutMapping
	@Operation(summary = "修改砍价商品")
	public Result<Boolean> updatePointsGoods(
		@RequestBody KanjiaActivityGoodsDTO kanJiaActivityGoodsDTO) {
		kanJiaActivityGoodsService.updateKanjiaActivityGoods(kanJiaActivityGoodsDTO);
		return Result.success();
	}

	@RequestLogger
	@PreAuthorize("hasAuthority('sys:resource:info:roleId')")
	@DeleteMapping("/{ids}")
	@Operation(summary = "删除砍价商品")
	public Result<Boolean> delete(@PathVariable String ids) {
		if (kanJiaActivityGoodsService.deleteKanJiaGoods(Arrays.asList(ids.split(",")))) {
			return Result.success(true);
		}
		throw new BusinessException(ResultEnum.KANJIA_GOODS_DELETE_ERROR);
	}


}
