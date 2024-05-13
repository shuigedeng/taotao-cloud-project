package com.taotao.cloud.jdbcpool.api;

import com.taotao.cloud.jdbcpool.api.IConfig;
import javax.sql.DataSource;

/**
 * 配置接口
 * @author shuigedeng
 * @since 1.0.0
 */
public interface IDataSourceConfig extends IConfig, DataSource {


}
