package com.taotao.cloud.sys.biz.tools.database.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sys.biz.tools.core.service.connect.ConnectService;
import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.database.dtos.TableMark;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableMarkService {
    // connName ==> catalog.schema.tableName ==> TableMark
    private Map<String,Map<ActualTableName, TableMark>> tableMarkMap = new HashMap<>();
    @Autowired
    private JdbcService jdbcService;
    @Autowired
    private FileManager fileManager;
    @Autowired
    private ConnectService connectService;

    /**
     * 配置表标签,直接覆盖的方式
     * @param tableMarks
     */
    public void configTableMark(Set<TableMark> tableMarks){
        Map<String, List<TableMark>> collect = tableMarks.stream().collect(Collectors.groupingBy(TableMark::getConnName));
        Iterator<Map.Entry<String, List<TableMark>>> iterator = collect.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, List<TableMark>> entry = iterator.next();
            String connName = entry.getKey();
            Map<ActualTableName, TableMark> actualTableNameTableMarkMap = tableMarkMap.computeIfAbsent(connName, k -> new HashMap<>());
            List<TableMark> value = entry.getValue();
            for (TableMark tableMark : value) {
                actualTableNameTableMarkMap.put(tableMark.getActualTableName(),tableMark);
            }
        }
        serializable();
    }

    /**
     * 所有数据序列化存储起来
     */
    private void serializable(){
        try {
            fileManager.writeConfig(JdbcService.MODULE,"metadata/tablemark", JSON.toJSONString(tableMarkMap));
        } catch (IOException e) {
            LogUtil.error("tableMark serializable error : {}",e.getMessage(),e);
        }
    }

    @PostConstruct
    private void init(){
        try {
            String tableMark = fileManager.readConfig(JdbcService.MODULE, "metadata/tablemark");
            if(StringUtils.isNotBlank(tableMark)){
                TypeReference<Map<String, Map<ActualTableName,TableMark>>> typeReference = new TypeReference<Map<String,Map<ActualTableName,TableMark>>>(){};
                tableMarkMap = JSON.parseObject(tableMark, typeReference);
            }
        } catch (IOException e) {
	        LogUtil.error("加载表标签失败:{}",e.getMessage());
        }
    }

    /**
     * 获取某个表的标签
     * @param connName
     * @return
     */
    public TableMark getTableMark(String connName,ActualTableName actualTableName){
        Map<ActualTableName, TableMark> actualTableNameTableMarkMap = tableMarkMap.computeIfAbsent(connName, k -> new HashMap<>());
        TableMark tableMark = actualTableNameTableMarkMap.get(actualTableName);
        if (tableMark == null){
            return new TableMark(connName,actualTableName);
        }
        return tableMark;
    }

    /**
     * 获取所有的表标签
     * @return
     */
    public Set<String> tags(){
        Set<String> tags = new HashSet<>();
        Iterator<Map<ActualTableName, TableMark>> iterator = tableMarkMap.values().iterator();
        while (iterator.hasNext()){
            Map<ActualTableName, TableMark> next = iterator.next();
            Iterator<TableMark> tableMarkIterator = next.values().iterator();
            while (tableMarkIterator.hasNext()){
                TableMark tableMark = tableMarkIterator.next();
                tags.addAll(tableMark.getTags());
            }
        }

        return tags;
    }

    /**
     * 查找有某个标签的表
     * @param connName
     * @param catalog
     * @param schemas
     * @param tag
     * @return
     * @throws SQLException
     * @throws IOException
     */
    public List<TableMetaData> searchTables(String connName,String catalog,Set<String> schemas,String tag) throws SQLException, IOException {
        List<TableMetaData> findTables = new ArrayList<>();

        List<TableMetaData> tableMetaDataList = jdbcService.filterSchemaTables(connName, catalog, schemas);
        Map<ActualTableName, TableMetaData> actualTableNameTableMetaDataMap = tableMetaDataList.stream().collect(Collectors.toMap(TableMetaData::getActualTableName, i -> i));

        Map<ActualTableName, TableMark> actualTableNameTableMarkMap = tableMarkMap.computeIfAbsent(connName,k -> new HashMap<>());
        Iterator<Map.Entry<ActualTableName, TableMark>> iterator = actualTableNameTableMarkMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<ActualTableName, TableMark> next = iterator.next();
            ActualTableName key = next.getKey();
            TableMark tableMark = next.getValue();
            ActualTableName actualTableName = tableMark.getActualTableName();
//            boolean tableNamespaceMatch = ((StringUtils.isNotBlank(catalog) && catalog.equals(actualTableName.getCatalog())) || StringUtils.isBlank(catalog))
//                    && ((CollectionUtils.isNotEmpty(schemas) && schemas.contains(actualTableName.getSchema())) || CollectionUtils.isEmpty(schemas));
            if (tableMark.getTags().contains(tag)){
                TableMetaData tableMetaData = actualTableNameTableMetaDataMap.get(actualTableName);
                if (tableMetaData != null) {
                    findTables.add(tableMetaData);
                }
            }
        }

        return findTables;
    }
}
