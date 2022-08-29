package com.taotao.cloud.web.configuration.bigdata;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.common.utils.context.ContextUtils;

public class BigDataSource {

	public static DruidDataSource getDefaultDataSource() {
		DruidDataSource find = ContextUtils.getBean(DruidDataSource.class, "tidbDataSource", false);
		if (find == null) {
			find = ContextUtils.getBean(DruidDataSource.class, "clickHouseDataSource", false);
		}
		return find;
	}
}
