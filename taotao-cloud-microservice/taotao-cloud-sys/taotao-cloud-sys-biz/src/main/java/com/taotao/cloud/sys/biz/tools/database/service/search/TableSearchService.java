package com.taotao.cloud.sys.biz.tools.database.service.search;


import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import java.util.List;

/**
 * 数据表搜索服务
 */
public interface TableSearchService {
    /**
     * 搜索数据表
     * @param connName
     * @param catalog
     * @param schemas
     * @param keyword
     * @return
     */
    List<TableMetaData> searchTables(String connName, String catalog, String[] schemas, String keyword) throws Exception;
}
