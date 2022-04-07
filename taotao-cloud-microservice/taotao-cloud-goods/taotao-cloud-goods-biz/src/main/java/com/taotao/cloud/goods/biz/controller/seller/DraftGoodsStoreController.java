package com.taotao.cloud.goods.biz.controller.seller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.taotao.cloud.common.constant.CommonConstant;
import com.taotao.cloud.common.enums.ResultEnum;
import com.taotao.cloud.common.exception.BusinessException;
import com.taotao.cloud.common.model.Result;
import com.taotao.cloud.common.utils.common.SecurityUtil;
import com.taotao.cloud.goods.api.dto.DraftGoodsDTO;
import com.taotao.cloud.goods.api.dto.DraftGoodsSearchParams;
import com.taotao.cloud.goods.api.vo.DraftGoodsVO;
import com.taotao.cloud.goods.biz.entity.DraftGoods;
import com.taotao.cloud.goods.biz.service.DraftGoodsService;
import com.taotao.cloud.logger.annotation.RequestLogger;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Objects;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Validated
@RestController
@Tag(name = "商户管理端-草稿商品API", description = "商户管理端-草稿商品API")
@RequestMapping("/goods/seller/store/goods/draftGoods")
public class DraftGoodsStoreController {

	/**
	 * 草稿商品服务
	 */
	private final DraftGoodsService draftGoodsService;

	@Operation(summary = "分页获取草稿商品列表", description = "分页获取草稿商品列表", method = CommonConstant.GET)
	@RequestLogger("分页获取草稿商品列表")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/page")
	public Result<IPage<DraftGoods>> getDraftGoodsByPage(
		DraftGoodsSearchParams searchParams) {
		String storeId = Objects.requireNonNull(SecurityUtil.getUser()).getStoreId();
		searchParams.setStoreId(storeId);
		return Result.success(draftGoodsService.getDraftGoods(searchParams));
	}

	@Operation(summary = "获取草稿商品", description = "获取草稿商品", method = CommonConstant.GET)
	@RequestLogger("获取草稿商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@GetMapping(value = "/{id}")
	public Result<DraftGoodsVO> getDraftGoods(@PathVariable String id) {
		return Result.success(draftGoodsService.getDraftGoods(id));
	}

	@Operation(summary = "保存草稿商品", description = "保存草稿商品", method = CommonConstant.POST)
	@RequestLogger("保存草稿商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@PostMapping
	public Result<Boolean> saveDraftGoods(@RequestBody DraftGoodsDTO draftGoodsVO) {
		//String storeId = Objects.requireNonNull(UserContext.getCurrentUser()).getStoreId();
		//if (draftGoodsVO.getStoreId() == null) {
		//	draftGoodsVO.setStoreId(storeId);
		//} else if (draftGoodsVO.getStoreId() != null && !storeId.equals(
		//	draftGoodsVO.getStoreId())) {
		//	throw new BusinessException(ResultEnum.USER_AUTHORITY_ERROR);
		//}
		//draftGoodsService.saveGoodsDraft(draftGoodsVO);
		return Result.success(true);
	}

	@Operation(summary = "删除草稿商品", description = "删除草稿商品", method = CommonConstant.DELETE)
	@RequestLogger("删除草稿商品")
	@PreAuthorize("hasAuthority('dept:tree:data')")
	@DeleteMapping(value = "/{id}")
	public Result<Boolean> deleteDraftGoods(@PathVariable String id) {
		draftGoodsService.getDraftGoods(id);
		draftGoodsService.deleteGoodsDraft(id);
		return Result.success(true);
	}

}
