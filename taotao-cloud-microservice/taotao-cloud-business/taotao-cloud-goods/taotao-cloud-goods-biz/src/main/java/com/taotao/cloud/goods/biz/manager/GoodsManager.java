package com.taotao.cloud.goods.biz.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.goods.biz.model.page.DraftGoodsPageQuery;
import com.taotao.cloud.goods.biz.model.page.GoodsPageQuery;
import com.taotao.cloud.goods.biz.mapper.IGoodsMapper;
import com.taotao.cloud.goods.biz.model.entity.Goods;
import com.taotao.boot.web.annotation.Manager;
import com.taotao.boot.webagg.manager.BaseManager;
import java.util.List;
import java.util.Objects;
import lombok.*;
import org.springframework.transaction.annotation.Transactional;

/**
 * 只能注入mapper或者dao  不能注入service
 *
 * @author shuigedeng
 * @version 2023.07
 * @see BaseManager
 * @since 2023-08-18 15:37:54
 */
@Manager
@AllArgsConstructor
public class GoodsManager extends BaseManager {

	private final IGoodsMapper goodsMapper;

	//测试用
	@Transactional(rollbackFor = Throwable.class)
	public void test(Goods goods, Goods swapGoods) {
		Long departmentSort = goods.getBrandId();
		goods.setBrandId(swapGoods.getBrandId());
		goodsMapper.updateById(goods);

		swapGoods.setBrandId(departmentSort);
		goodsMapper.updateById(swapGoods);
	}

	public <T> QueryWrapper<T> goodsQueryWrapper(GoodsPageQuery goodsPageQuery) {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(Objects.nonNull(goodsPageQuery.getGoodsId()), "goods_id",
			goodsPageQuery.getGoodsId());
		queryWrapper.like(
			StrUtil.isNotEmpty(goodsPageQuery.getGoodsName()),
			"goods_name",
			goodsPageQuery.getGoodsName());
		queryWrapper.in(
			StrUtil.isNotEmpty(goodsPageQuery.getId()),
			"id",
			List.of(goodsPageQuery.getId().split(",")));
		queryWrapper.eq(Objects.nonNull(goodsPageQuery.getStoreId()), "store_id",
			goodsPageQuery.getStoreId());
		queryWrapper.like(
			StrUtil.isNotEmpty(goodsPageQuery.getStoreName()),
			"store_name",
			goodsPageQuery.getStoreName());
		queryWrapper.like(
			StrUtil.isNotEmpty(goodsPageQuery.getCategoryPath()),
			"category_path",
			goodsPageQuery.getCategoryPath());
		queryWrapper.like(
			StrUtil.isNotEmpty(goodsPageQuery.getStoreCategoryPath()),
			"store_category_path",
			goodsPageQuery.getStoreCategoryPath());
		queryWrapper.eq(goodsPageQuery.getSelfOperated() != null, "self_operated",
			goodsPageQuery.getSelfOperated());
		queryWrapper.eq(
			StrUtil.isNotEmpty(goodsPageQuery.getMarketEnable()),
			"market_enable",
			goodsPageQuery.getMarketEnable());
		queryWrapper.eq(
			StrUtil.isNotEmpty(goodsPageQuery.getAuthFlag()), "auth_flag",
			goodsPageQuery.getAuthFlag());
		queryWrapper.le(goodsPageQuery.getLeQuantity() != null, "quantity",
			goodsPageQuery.getLeQuantity());
		queryWrapper.ge(goodsPageQuery.getGeQuantity() != null, "quantity",
			goodsPageQuery.getGeQuantity());
		queryWrapper.le(goodsPageQuery.getRecommend() != null, "recommend",
			goodsPageQuery.getRecommend());
		queryWrapper.eq(
			StrUtil.isNotEmpty(goodsPageQuery.getGoodsType()),
			"goods_type",
			goodsPageQuery.getGoodsType());
		queryWrapper.eq("delete_flag", false);
		goodsBetweenWrapper(queryWrapper, goodsPageQuery);
		return queryWrapper;
	}

	private <T> void goodsBetweenWrapper(QueryWrapper<T> queryWrapper,
		GoodsPageQuery goodsPageQuery) {
		if (StrUtil.isNotEmpty(goodsPageQuery.getPrice())) {
			String[] s = goodsPageQuery.getPrice().split("_");
			if (s.length > 1) {
				queryWrapper.between("price", s[0], s[1]);
			}
			else {
				queryWrapper.ge("price", s[0]);
			}
		}
	}

	public <T> QueryWrapper<T> draftGoodsQueryWrapper(DraftGoodsPageQuery draftGoodsPageQuery) {
		QueryWrapper<T> queryWrapper = goodsQueryWrapper(draftGoodsPageQuery);
		if (StrUtil.isNotEmpty(draftGoodsPageQuery.getSaveType())) {
			queryWrapper.eq("save_type", draftGoodsPageQuery.getSaveType());
		}
		return queryWrapper;
	}
}
