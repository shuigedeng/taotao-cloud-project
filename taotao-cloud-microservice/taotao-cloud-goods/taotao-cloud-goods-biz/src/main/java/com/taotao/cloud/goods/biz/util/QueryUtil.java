package com.taotao.cloud.goods.biz.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.goods.api.model.query.DraftGoodsPageQuery;
import com.taotao.cloud.goods.api.model.query.GoodsPageQuery;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class QueryUtil {
	 public static  <T> QueryWrapper<T> goodsQueryWrapper(GoodsPageQuery goodsPageQuery) {
	 	QueryWrapper<T> queryWrapper = new QueryWrapper<>();
	 	queryWrapper.eq(Objects.nonNull(goodsPageQuery.getGoodsId()), "goods_id", goodsPageQuery.getGoodsId());
	 	queryWrapper.like(CharSequenceUtil.isNotEmpty(goodsPageQuery.getGoodsName()), "goods_name", goodsPageQuery.getGoodsName());
	 	queryWrapper.in(CharSequenceUtil.isNotEmpty(goodsPageQuery.getId()), "id",
		    List.of(goodsPageQuery.getId().split(",")));
	 	queryWrapper.eq(Objects.nonNull(goodsPageQuery.getStoreId()), "store_id", goodsPageQuery.getStoreId());
	 	queryWrapper.like(CharSequenceUtil.isNotEmpty(goodsPageQuery.getStoreName()), "store_name", goodsPageQuery.getStoreName());
	 	queryWrapper.like(CharSequenceUtil.isNotEmpty(goodsPageQuery.getCategoryPath()), "category_path", goodsPageQuery.getCategoryPath());
	 	queryWrapper.like(CharSequenceUtil.isNotEmpty(goodsPageQuery.getStoreCategoryPath()),"store_category_path", goodsPageQuery.getStoreCategoryPath());
	 	queryWrapper.eq(goodsPageQuery.getSelfOperated() != null, "self_operated", goodsPageQuery.getSelfOperated());
	 	queryWrapper.eq(CharSequenceUtil.isNotEmpty(goodsPageQuery.getMarketEnable()), "market_enable", goodsPageQuery.getMarketEnable());
	 	queryWrapper.eq(CharSequenceUtil.isNotEmpty(goodsPageQuery.getAuthFlag()), "auth_flag", goodsPageQuery.getAuthFlag());
	 	queryWrapper.le(goodsPageQuery.getLeQuantity() != null, "quantity", goodsPageQuery.getLeQuantity());
	 	queryWrapper.ge(goodsPageQuery.getGeQuantity() != null, "quantity", goodsPageQuery.getGeQuantity());
	 	queryWrapper.le(goodsPageQuery.getRecommend() != null, "recommend", goodsPageQuery.getRecommend());
	 	queryWrapper.eq(CharSequenceUtil.isNotEmpty(goodsPageQuery.getGoodsType()), "goods_type", goodsPageQuery.getGoodsType());
	 	queryWrapper.eq("delete_flag", false);
		 goodsBetweenWrapper(queryWrapper, goodsPageQuery);
	 	return queryWrapper;
	 }

	 private static  <T> void goodsBetweenWrapper(QueryWrapper<T> queryWrapper, GoodsPageQuery goodsPageQuery) {
	 	if (CharSequenceUtil.isNotEmpty(goodsPageQuery.getPrice())) {
	 		String[] s = goodsPageQuery.getPrice().split("_");
	 		if (s.length > 1) {
	 			queryWrapper.between("price", s[0], s[1]);
	 		} else {
	 			queryWrapper.ge("price", s[0]);
	 		}
	 	}
	 }

	 public static <T> QueryWrapper<T> draftGoodsQueryWrapper(DraftGoodsPageQuery draftGoodsPageQuery) {
	 	QueryWrapper<T> queryWrapper = goodsQueryWrapper(draftGoodsPageQuery);
	 	if (StrUtil.isNotEmpty(draftGoodsPageQuery.getSaveType())) {
	 		queryWrapper.eq("save_type", draftGoodsPageQuery.getSaveType());
	 	}
	 	return queryWrapper;
	 }
}
