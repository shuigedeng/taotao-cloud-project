package com.taotao.cloud.goods.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.goods.api.dto.DraftGoodsDTO;
import com.taotao.cloud.goods.api.dto.DraftGoodsSearchParams;
import com.taotao.cloud.goods.api.vo.DraftGoodsVO;
import com.taotao.cloud.goods.biz.entity.DraftGoods;
import com.taotao.cloud.goods.biz.service.DraftGoodsService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import org.apache.shardingsphere.distsql.parser.autogen.CommonDistSQLStatementParser.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
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
 */
@Validated
@RestController
@Tag(name = "商户管理端-草稿商品API", description = "商户管理端-草稿商品API")
@RequestMapping("/goods/seller/store/goods/draftGoods")
public class DraftGoodsStoreController {

	@Autowired
	private DraftGoodsService draftGoodsService;

	@Operation(summary = "分页获取草稿商品列表", description = "分页获取草稿商品列表", method = CommonConstant.GET)
	@RequestLogger(description = "分页获取草稿商品列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<IPage<DraftGoods>> getDraftGoodsByPage(
		DraftGoodsSearchParams searchParams) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		searchParams.setStoreId(storeId);
		return Result.success(draftGoodsService.getDraftGoods(searchParams));
	}

	@Operation(summary = "获取草稿商品", description = "获取草稿商品", method = CommonConstant.GET)
	@RequestLogger(description = "获取草稿商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<DraftGoodsVO> getDraftGoods(@PathVariable String id) {
		DraftGoodsVO draftGoods = OperationalJudgment.judgment(draftGoodsService.getDraftGoods(id));
		return Result.success(draftGoods);
	}

	@Operation(summary = "保存草稿商品", description = "保存草稿商品", method = CommonConstant.POST)
	@RequestLogger(description = "保存草稿商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<String> saveDraftGoods(@RequestBody DraftGoodsDTO draftGoodsVO) {
		String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		if (draftGoodsVO.getStoreId() == null) {
			draftGoodsVO.setStoreId(storeId);
		} else if (draftGoodsVO.getStoreId() != null && !storeId.equals(
			draftGoodsVO.getStoreId())) {
			throw new ServiceException(ResultCode.USER_AUTHORITY_ERROR);
		}
		draftGoodsService.saveGoodsDraft(draftGoodsVO);
		return Result.success();
	}

	@Operation(summary = "删除草稿商品", description = "删除草稿商品", method = CommonConstant.DELETE)
	@RequestLogger(description = "删除草稿商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<String> deleteDraftGoods(@PathVariable String id) {
		OperationalJudgment.judgment(draftGoodsService.getDraftGoods(id));
		draftGoodsService.deleteGoodsDraft(id);
		return Result.success();
	}

}
