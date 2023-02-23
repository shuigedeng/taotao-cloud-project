package com.taotao.cloud.flowable.biz.utils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taotao.cloud.common.utils.date.DateUtils;
import com.taotao.cloud.common.utils.lang.StringUtils;
import com.taotao.cloud.customer.api.model.query.StorePageQuery;
import com.taotao.cloud.store.api.enums.StoreStatusEnum;

public class QueryUtil {
	 public static  <T> QueryWrapper<T> queryWrapper(StorePageQuery storePageQuery) {
	     QueryWrapper<T> queryWrapper = new QueryWrapper<>();
	     if (StringUtils.isNotEmpty(storePageQuery.getStoreName())) {
	         queryWrapper.like("store_name", storePageQuery.getStoreName());
	     }
	     if (StringUtils.isNotEmpty(storePageQuery.getMemberName())) {
	         queryWrapper.like("member_name", storePageQuery.getMemberName());
	     }
	     if (StringUtils.isNotEmpty(storePageQuery.getStoreDisable())) {
	         queryWrapper.eq("store_disable", storePageQuery.getStoreDisable());
	     } else {
	         queryWrapper.eq("store_disable", StoreStatusEnum.OPEN.name()).or().eq("store_disable", StoreStatusEnum.CLOSED.name());
	     }
	     //按时间查询
	     if (StringUtils.isNotEmpty(storePageQuery.getStartDate())) {
	         queryWrapper.ge("create_time", DateUtils.parse(storePageQuery.getStartDate()));
	     }
	     if (StringUtils.isNotEmpty(storePageQuery.getEndDate())) {
	         queryWrapper.le("create_time", DateUtils.parse(storePageQuery.getEndDate()));
	     }
	     return queryWrapper;
	 }
}
