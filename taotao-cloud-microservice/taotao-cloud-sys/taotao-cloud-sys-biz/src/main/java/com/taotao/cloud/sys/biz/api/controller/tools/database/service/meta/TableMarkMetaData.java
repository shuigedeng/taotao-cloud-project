package com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.taotao.cloud.sys.biz.api.controller.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableMark;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.search.SearchParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TableMarkMetaData {
    /**
     * 表标记存储结构
     * connName ==> catalog.schema.tableName ==> TableMark
     */
    private Map<String, Map<ActualTableName, TableMark>> tableMarkMap = new HashMap<>();

    @Autowired
    private FileManager fileManager;

    /**
     * 配置表标签,直接覆盖的方式
     * @param tableMarks
     */
    public void configTableMark(String connName,Set<TableMark> tableMarks){
        final Map<ActualTableName, TableMark> connTableMark = tableMarks.stream().collect(Collectors.toMap(TableMark::getActualTableName, Function.identity()));
        final Map<ActualTableName, TableMark> actualTableNameTableMarkMap = tableMarkMap.computeIfAbsent(connName, k -> new HashMap<>());

        final Iterator<ActualTableName> iterator = connTableMark.keySet().iterator();
        while (iterator.hasNext()){
            final ActualTableName actualTableName = iterator.next();
            actualTableNameTableMarkMap.put(actualTableName,connTableMark.get(actualTableName));
        }

        serializable();
    }

    /**
     * 所有数据序列化存储起来
     */
    private void serializable(){
        try {
            fileManager.writeConfig("database","metadata/tablemark", JSON.toJSONString(tableMarkMap));
        } catch (IOException e) {
            log.error("tableMark serializable error : {}",e.getMessage(),e);
        }
    }

    @PostConstruct
    private void init(){
        try {
            String tableMark = fileManager.readConfig("database", "metadata/tablemark");
            if(StringUtils.isNotBlank(tableMark)){
                TypeReference<Map<String, Map<ActualTableName,TableMark>>> typeReference = new TypeReference<Map<String,Map<ActualTableName,TableMark>>>(){};
                tableMarkMap = JSON.parseObject(tableMark, typeReference);
            }
        } catch (IOException e) {
            log.error("加载表标签失败:{}",e.getMessage());
        }
    }

    /**
     * 获取某个表的标签
     * @param connName
     * @param schemaName
     * @param tableName
     * @return
     */
    public TableMark getTableMark(String connName,ActualTableName actualTableName){
        Map<ActualTableName, TableMark> actualTableNameTableMarkMap = tableMarkMap.computeIfAbsent(connName, k -> new HashMap<>());
        TableMark tableMark = actualTableNameTableMarkMap.get(actualTableName);
        if (tableMark == null){
            return new TableMark(actualTableName);
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
    public List<TableMeta> searchTables(List<TableMeta> firstFilterTables,String connName, SearchParam searchParam) throws SQLException, IOException {
        List<TableMeta> findTables = new ArrayList<>();

        final String keyword = searchParam.getKeyword();
        final String[] keywordPart = StringUtils.split(keyword, ',');

        Map<ActualTableName, TableMeta> actualTableNameTableMetaDataMap = firstFilterTables.stream().collect(Collectors.toMap(tableMeta -> tableMeta.getTable().getActualTableName(), i -> i));

        Map<ActualTableName, TableMark> actualTableNameTableMarkMap = tableMarkMap.computeIfAbsent(connName,k -> new HashMap<>());
        Iterator<Map.Entry<ActualTableName, TableMark>> iterator = actualTableNameTableMarkMap.entrySet().iterator();
        A:while (iterator.hasNext()){
            Map.Entry<ActualTableName, TableMark> next = iterator.next();
            ActualTableName key = next.getKey();
            TableMark tableMark = next.getValue();
            ActualTableName actualTableName = tableMark.getActualTableName();
            for (String partTag : keywordPart) {
                if (tableMark.getTags().contains(partTag)){
                    TableMeta tableMetaData = actualTableNameTableMetaDataMap.get(actualTableName);
                    if (tableMetaData != null) {
                        findTables.add(tableMetaData);
                        continue A;
                    }
                }
            }

        }

        return findTables;
    }
}
