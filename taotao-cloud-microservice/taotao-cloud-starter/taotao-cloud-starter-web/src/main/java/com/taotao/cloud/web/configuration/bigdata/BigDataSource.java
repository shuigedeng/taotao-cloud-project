package com.taotao.cloud.web.configuration.bigdata;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.common.utils.context.ContextUtil;

public class BigDataSource {

	public static DruidDataSource getDefaultDataSource() {
		DruidDataSource find = ContextUtil.getBean(DruidDataSource.class, "tidbDataSource", false);
		if (find == null) {
			find = ContextUtil.getBean(DruidDataSource.class, "clickHouseDataSource", false);
		}
		return find;
	}
}
