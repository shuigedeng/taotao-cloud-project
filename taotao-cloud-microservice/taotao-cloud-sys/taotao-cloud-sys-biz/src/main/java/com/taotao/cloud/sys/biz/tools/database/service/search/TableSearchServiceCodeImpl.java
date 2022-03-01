package com.taotao.cloud.sys.biz.tools.database.service.search;

import com.taotao.cloud.sys.biz.tools.database.dtos.ExtendTableMetaData;
import com.taotao.cloud.sys.biz.tools.database.dtos.TableMark;
import com.taotao.cloud.sys.biz.tools.database.service.JdbcService;
import com.taotao.cloud.sys.biz.tools.database.service.TableMarkService;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 硬编码的数据表搜索服务
 */
@Service
public class TableSearchServiceCodeImpl implements TableSearchService{
    @Autowired
    private TableMarkService tableMarkService;
    @Autowired
    private JdbcService jdbcService;

    /**
     * 元数据表搜索功能
     * @param connName
     * @param catalog
     * @param schemas
     * @param keyword
     * @return
     */
    public List<ExtendTableMetaData> searchTablesEx(String connName, String catalog, String[] schemas, String keyword) throws Exception {
        final List<TableMetaData> tableMetaDataList = this.searchTables(connName, catalog, schemas, keyword);

        List<ExtendTableMetaData> extendTableMetaData = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tableMetaDataList)){
            for (TableMetaData tableMetaData : tableMetaDataList) {
                ActualTableName actualTableName = tableMetaData.getActualTableName();
                TableMark tableMark = tableMarkService.getTableMark(connName, actualTableName);
                extendTableMetaData.add(new ExtendTableMetaData(tableMetaData,tableMark));
            }
        }

        return extendTableMetaData;
    }

    @Override
    public List<TableMetaData> searchTables(String connName, String catalog, String[] schemas, String keyword) throws Exception {
        // 根据关键字进行过滤
        String searchSchema = "";
        if(StringUtils.isNotBlank(keyword) && keyword.contains(":")){
            String [] array = keyword.split(":",2);
            searchSchema = array[0];
            keyword = array[1];
        }
        Set<String> schemasSet = Arrays.stream(schemas).collect(Collectors.toSet());
        if (StringUtils.isNotBlank(keyword) && keyword.contains(".")){
            // 如果包含 . 的话, 前面是 schema , 后面是 keyword
            String [] array = keyword.split("\\.",2);
            String schema = array[0];
            schemasSet.add(schema);
            keyword = array[1];
        }

        List<TableMetaData> tableMetaDataList = null;
        if (StringUtils.isNotBlank(searchSchema) && "tag".equals(searchSchema)){
            tableMetaDataList = tableMarkService.searchTables(connName,catalog,schemasSet,keyword);
        }else {
            tableMetaDataList = jdbcService.searchTables(connName, catalog, schemasSet,searchSchema, keyword);
        }
        return tableMetaDataList;
    }
}
