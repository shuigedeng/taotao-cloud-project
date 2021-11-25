package com.taotao.cloud.bigdata.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.taotao.cloud.common.utils.ContextUtil;

public class BigDataSource {

	public static DruidDataSource getDefaultDataSource() {
		DruidDataSource find = ContextUtil.getBean(DruidDataSource.class, "tidbDataSource", false);
		if (find == null) {
			find = ContextUtil.getBean(DruidDataSource.class, "clickHouseDataSource", false);
		}
		return find;
	}
}
