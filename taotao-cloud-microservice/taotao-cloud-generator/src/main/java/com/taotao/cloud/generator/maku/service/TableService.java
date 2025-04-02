package com.taotao.cloud.generator.maku.service;

import net.maku.generator.common.page.PageResult;
import net.maku.generator.common.query.Query;
import net.maku.generator.common.service.BaseService;
import net.maku.generator.entity.TableEntity;

/**
 * 数据表
 *
 * @author 阿沐 babamu@126.com
 * <a href="https://maku.net">MAKU</a>
 */
public interface TableService extends BaseService<TableEntity> {

    PageResult<TableEntity> page(Query query);

    TableEntity getByTableName(String tableName);

    void deleteBatchIds(Long[] ids);

    /**
     * 导入表
     *
     * @param datasourceId 数据源ID
     * @param tableName    表名
     */
    void tableImport(Long datasourceId, String tableName);

    /**
     * 同步数据库表
     *
     * @param id 表ID
     */
    void sync(Long id);
}
