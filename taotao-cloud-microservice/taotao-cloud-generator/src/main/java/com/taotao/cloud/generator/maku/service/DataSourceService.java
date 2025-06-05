package com.taotao.cloud.generator.maku.service;

import com.taotao.cloud.generator.maku.common.page.PageResult;
import com.taotao.cloud.generator.maku.common.query.Query;
import com.taotao.cloud.generator.maku.common.service.BaseService;
import com.taotao.cloud.generator.maku.config.GenDataSource;
import com.taotao.cloud.generator.maku.entity.DataSourceEntity;

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
