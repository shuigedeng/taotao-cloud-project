package com.taotao.cloud.goods.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.PageModel;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.goods.api.dto.DraftGoodsDTO;
import com.taotao.cloud.goods.api.query.DraftGoodsPageQuery;
import com.taotao.cloud.goods.api.vo.DraftGoodsBaseVO;
import com.taotao.cloud.goods.api.vo.DraftGoodsVO;
import com.taotao.cloud.goods.biz.entity.DraftGoods;
import com.taotao.cloud.goods.biz.service.IDraftGoodsService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 店铺端,草稿商品接口
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 22:05:35
 */
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "店铺端-草稿商品API", description = "店铺端-草稿商品API")
@RequestMapping("/goods/seller/draft/goods")
public class DraftGoodsStoreController {

	/**
	 * 草稿商品服务
	 */
	private final IDraftGoodsService draftGoodsService;

	@Operation(summary = "分页获取草稿商品列表", description = "分页获取草稿商品列表")
	@RequestLogger("分页获取草稿商品列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<PageModel<DraftGoodsBaseVO>> getDraftGoodsByPage(DraftGoodsPageQuery draftGoodsPageQuery) {
		Long storeId = SecurityUtil.getUser().getStoreId();
		draftGoodsPageQuery.setStoreId(storeId);
		IPage<DraftGoods> draftGoods = draftGoodsService.getDraftGoods(draftGoodsPageQuery);
		return Result.success(PageModel.convertMybatisPage(draftGoods, DraftGoodsBaseVO.class));
	}

	@Operation(summary = "获取草稿商品", description = "获取草稿商品")
	@RequestLogger("获取草稿商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<DraftGoodsVO> getDraftGoods(@PathVariable Long id) {
		return Result.success(draftGoodsService.getDraftGoods(id));
	}

	@Operation(summary = "保存草稿商品", description = "保存草稿商品")
	@RequestLogger("保存草稿商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> saveDraftGoods(@Validated @RequestBody DraftGoodsDTO draftGoodsDTO) {
		Long storeId = SecurityUtil.getUser().getStoreId();
		if (draftGoodsDTO.getStoreId() == null) {
			draftGoodsDTO.setStoreId(storeId);
		} else if (draftGoodsDTO.getStoreId() != null && !storeId.equals(
			draftGoodsDTO.getStoreId())) {
			throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
		}
		return Result.success(draftGoodsService.saveGoodsDraft(draftGoodsDTO));
	}

	@Operation(summary = "删除草稿商品", description = "删除草稿商品")
	@RequestLogger("删除草稿商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> deleteDraftGoods(@PathVariable Long id) {
		draftGoodsService.getDraftGoods(id);
		return Result.success(draftGoodsService.deleteGoodsDraft(id));
	}

}
