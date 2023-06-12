package com.taotao.cloud.promotion.biz.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.promotion.api.enums.PromotionsStatusEnum;
import com.taotao.cloud.promotion.api.model.page.BasePromotionsSearchQuery;
import com.taotao.cloud.promotion.api.model.page.KanjiaActivityGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.page.PointsGoodsPageQuery;
import com.taotao.cloud.promotion.api.model.query.KanjiaActivitySearchQuery;
import com.taotao.cloud.promotion.biz.model.pojo.PromotionTools;

import java.util.Date;

public class PageQueryUtils {

	public static <T> QueryWrapper<T> queryWrapper(BasePromotionsSearchQuery pageQuery) {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();

		if (CharSequenceUtil.isNotEmpty(pageQuery.getId())) {
			queryWrapper.eq("id", pageQuery.getId());
		}
		if (pageQuery.getStartTime() != null) {
			queryWrapper.ge("start_time", new Date(pageQuery.getStartTime()));
		}
		if (pageQuery.getEndTime() != null) {
			queryWrapper.le("end_time", new Date(pageQuery.getEndTime()));
		}
		if (CharSequenceUtil.isNotEmpty(pageQuery.getPromotionStatus())) {
			queryWrapper.and(
				PromotionTools.queryPromotionStatus(PromotionsStatusEnum.valueOf(pageQuery.getPromotionStatus())));
		}
		if (CharSequenceUtil.isNotEmpty(pageQuery.getScopeType())) {
			queryWrapper.eq("scope_type", pageQuery.getScopeType());
		}
		queryWrapper.eq("delete_flag", false);
		return queryWrapper;
	}

	public static <T> QueryWrapper<T> pointsGoodsPageQuery(PointsGoodsPageQuery pageQuery) {
		QueryWrapper<T> queryWrapper = queryWrapper(pageQuery.baseSearchQuery());
		if (CharSequenceUtil.isNotEmpty(pageQuery.getGoodsName())) {
			queryWrapper.like("goods_name", pageQuery.getGoodsName());
		}
		if (CharSequenceUtil.isNotEmpty(pageQuery.getSkuId())) {
			queryWrapper.eq("sku_id", pageQuery.getSkuId());
		}
		if (CharSequenceUtil.isNotEmpty(pageQuery.getPointsGoodsCategoryId())) {
			queryWrapper.eq("points_goods_category_id", pageQuery.getPointsGoodsCategoryId());
		}
		if (CharSequenceUtil.isNotEmpty(pageQuery.getPoints())) {
			String[] s = pageQuery.getPoints().split("_");
			if (s.length > 1) {
				queryWrapper.between("points", s[0], s[1]);
			} else {
				queryWrapper.eq("points", s[0]);
			}
		}
		return queryWrapper;
	}

	public static  <T> QueryWrapper<T> kanjiaActivityGoodsPageQueryWrapper(KanjiaActivityGoodsPageQuery query) {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();

		if (CharSequenceUtil.isNotEmpty(query.getGoodsName())) {
			queryWrapper.like("goods_name", query.getGoodsName());
		}
		if (query.getPromotionStatus() != null) {
			queryWrapper.and(
				PromotionTools.queryPromotionStatus(PromotionsStatusEnum.valueOf(query.getPromotionStatus() )));
		}
		if (query.getStartTime() != null) {
			queryWrapper.le("start_time", new Date(query.getStartTime()));
		}
		if (query.getEndTime() != null) {
			queryWrapper.ge("end_time", new Date(query.getEndTime()));
		}
		//if (UserContext.getCurrentUser() != null &&
		//UserContext.getCurrentUser().getRole().equals(UserEnums.MEMBER)) {
		//    queryWrapper.gt("stock", 0);
		//}
		queryWrapper.eq("delete_flag", false);
		return queryWrapper;
	}

	public static <T> QueryWrapper<T> kanjiaActivitySearchQuerywrapper(KanjiaActivitySearchQuery query) {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(StrUtil.isNotEmpty(query.getKanjiaActivityId()), "id", query.getKanjiaActivityId());
		queryWrapper.eq(StrUtil.isNotEmpty(query.getKanjiaActivityGoodsId()), "kanjia_activity_goods_id",
			query.getKanjiaActivityGoodsId());
		queryWrapper.eq(StrUtil.isNotEmpty(query.getGoodsSkuId()), "sku_id", query.getGoodsSkuId());
		queryWrapper.eq(StrUtil.isNotEmpty(query.getMemberId()), "member_id", query.getMemberId());
		queryWrapper.eq(StrUtil.isNotEmpty(query.getStatus()), "status", query.getStatus());
		return queryWrapper;
	}
}
