package com.taotao.cloud.generator.maku.service;

import net.maku.generator.common.page.PageResult;
import net.maku.generator.common.query.Query;
import net.maku.generator.common.service.BaseService;
import net.maku.generator.config.GenDataSource;
import net.maku.generator.entity.DataSourceEntity;

import java.util.List;

/**
 * 数据源管理
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface DataSourceService extends BaseService<DataSourceEntity> {

    PageResult<DataSourceEntity> page(Query query);

    List<DataSourceEntity> getList();

    /**
     * 获取数据库产品名，如：MySQL
     *
     * @param datasourceId 数据源ID
     * @return 返回产品名
     */
    String getDatabaseProductName(Long datasourceId);

    /**
     * 根据数据源ID，获取数据源
     *
     * @param datasourceId 数据源ID
     */
    GenDataSource get(Long datasourceId);
}
