package com.taotao.cloud.order.biz.manager;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.boot.common.enums.UserEnum;
import com.taotao.boot.security.spring.utils.SecurityUtils;
import com.taotao.boot.common.utils.lang.StringUtils;
import com.taotao.cloud.order.sys.model.page.aftersale.AfterSalePageQuery;
import com.taotao.cloud.order.biz.mapper.aftersale.AfterSaleMapper;
import com.taotao.boot.web.annotation.Manager;
import com.taotao.boot.webagg.manager.BaseManager;
import lombok.*;

/**
 * 售后管理器
 */
@AllArgsConstructor
@Manager
public class AfterSaleManager extends BaseManager {
	private final AfterSaleMapper afterSaleMapper;

	public <T> QueryWrapper<T> queryWrapper(AfterSalePageQuery afterSalePageQuery) {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (StringUtils.isNotEmpty(afterSalePageQuery.getSn())) {
			queryWrapper.like("sn", afterSalePageQuery.getSn());
		}
		if (StringUtils.isNotEmpty(afterSalePageQuery.getOrderSn())) {
			queryWrapper.like("order_sn", afterSalePageQuery.getOrderSn());
		}

		// 按买家查询
		if (SecurityUtils.getCurrentUser().getType() == UserEnum.MEMBER.getCode()) {
			queryWrapper.eq("member_id", SecurityUtils.getCurrentUser().getUserId());
		}

		// 按卖家查询
		if (SecurityUtils.getCurrentUser().getType() == UserEnum.STORE.getCode()) {
			queryWrapper.eq("store_id", SecurityUtils.getCurrentUser().getStoreId());
		}

		if (SecurityUtils.getCurrentUser().getType() == UserEnum.MANAGER.getCode()
			&& StringUtils.isNotEmpty(afterSalePageQuery.getStoreId())) {
			queryWrapper.eq("store_id", afterSalePageQuery.getStoreId());
		}
		if (StringUtils.isNotEmpty(afterSalePageQuery.getMemberName())) {
			queryWrapper.like("member_name", afterSalePageQuery.getMemberName());
		}
		if (StringUtils.isNotEmpty(afterSalePageQuery.getStoreName())) {
			queryWrapper.like("store_name", afterSalePageQuery.getStoreName());
		}
		if (StringUtils.isNotEmpty(afterSalePageQuery.getGoodsName())) {
			queryWrapper.like("goods_name", afterSalePageQuery.getGoodsName());
		}
		// 按时间查询
		if (afterSalePageQuery.getStartDate() != null) {
			queryWrapper.ge("create_time", afterSalePageQuery.getStartDate());
		}
		if (afterSalePageQuery.getEndDate() != null) {
			queryWrapper.le("create_time", afterSalePageQuery.getEndDate());
		}
		if (StringUtils.isNotEmpty(afterSalePageQuery.getServiceStatus())) {
			queryWrapper.eq("service_status", afterSalePageQuery.getServiceStatus());
		}
		if (StringUtils.isNotEmpty(afterSalePageQuery.getServiceType())) {
			queryWrapper.eq("service_type", afterSalePageQuery.getServiceType());
		}
		betweenWrapper(queryWrapper, afterSalePageQuery);
		queryWrapper.eq("delete_flag", false);
		return queryWrapper;
	}

	private <T> void betweenWrapper(QueryWrapper<T> queryWrapper, AfterSalePageQuery afterSalePageQuery) {
		if (StringUtils.isNotEmpty(afterSalePageQuery.getApplyRefundPrice())) {
			String[] s = afterSalePageQuery.getApplyRefundPrice().split("_");
			if (s.length > 1) {
				queryWrapper.ge("apply_refund_price", s[1]);
			} else {
				queryWrapper.le("apply_refund_price", s[0]);
			}
		}
		if (StringUtils.isNotEmpty(afterSalePageQuery.getActualRefundPrice())) {
			String[] s = afterSalePageQuery.getActualRefundPrice().split("_");
			if (s.length > 1) {
				queryWrapper.ge("actual_refund_price", s[1]);
			} else {
				queryWrapper.le("actual_refund_price", s[0]);
			}
		}
	}
}
