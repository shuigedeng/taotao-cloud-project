package com.taotao.cloud.sys.biz.tools.database.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.sys.biz.tools.core.service.file.FileManager;
import com.taotao.cloud.sys.biz.tools.database.dtos.TableRelationDto;
import com.taotao.cloud.sys.biz.tools.database.dtos.TableRelationTree;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.Column;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 表关系管理
 */
@Service
public class TableRelationService {
    @Autowired
    private FileManager fileManager;

    // 连接名 = > catalog => 表关系列表
    private static Map<String,Map<String, Set<TableRelationDto>>> tableRelationDtoMap =  new HashMap<>();

    /**
     * 新增关系
     * @param connName
     * @param catalog
     * @param TableRelationDtos
     */
    public void configRelation(String connName, String catalog, Set<TableRelationDto> TableRelationDtos){
        Set<TableRelationDto> relations = loadCatalogRelationMap(connName, catalog);
        relations.addAll(TableRelationDtos);

        serializable();
    }

    /**
     * 删除关系
     * @param connName
     * @param catalog
     * @param TableRelationDtos
     */
    public void drop(String connName,String catalog,Set<TableRelationDto> TableRelationDtos){
        Set<TableRelationDto> relations = loadCatalogRelationMap(connName, catalog);
        relations.removeAll(TableRelationDtos);
    }

    /**
     * 查询某个表使用其它表的表关系
     * @param connName
     * @return
     */
    public List<TableRelationDto> childs(String connName, ActualTableName actualTableName){
        Set<TableRelationDto> relations = loadCatalogRelationMap(connName, actualTableName.getCatalog());
        return relations.stream().filter(relation -> relation.getSourceTableName().equals(actualTableName)).collect(Collectors.toList());
    }

    /**
     * 查询使用某个表的表关系
     * @param connName
     * @param tableName
     * @return
     */
    public List<TableRelationDto> parents(String connName,ActualTableName tableName){
        String catalog = tableName.getCatalog();
        Set<TableRelationDto> relations = loadCatalogRelationMap(connName, catalog);
        Predicate<TableRelationDto> function = relation -> relation.getTargetTableName().equals(tableName)
                || TableRelationDto.Relation.valueOf(relation.getRelation()) != TableRelationDto.Relation.ONE_MANY;
        return relations.stream().filter(function).collect(Collectors.toList());
    }

    /**
     * 表引用层级
     * @param connName
     * @param catalog
     * @param tableName
     * @return
     */
    public TableRelationTree hierarchy(String connName, ActualTableName actualTableName) {
        Set<TableRelationDto> relations = loadCatalogRelationMap(connName, actualTableName.getCatalog());

        TableRelationTree top = new TableRelationTree(actualTableName);
        findTableHierarchy(relations,top);
        return top;
    }

    private void findTableHierarchy(Set<TableRelationDto> relations, TableRelationTree parent) {
        ActualTableName originTable = parent.getTableName();
        for (TableRelationDto relation : relations) {
            ActualTableName sourceTableName = relation.getSourceTableName();
            if(sourceTableName.equals(originTable)){
                TableRelationDto.Relation currentRelation = TableRelationDto.Relation.valueOf(relation.getRelation());
                TableRelationTree subParent = new TableRelationTree(relation.getSourceColumnName(),relation.getTargetTableName(),relation.getTargetColumnName(),currentRelation);
                parent.addRelation(subParent);
                findTableHierarchy(relations,subParent);
            }
        }
    }

    public TableRelationTree superTypes(String connName, ActualTableName actualTableName) {
        Set<TableRelationDto> relations = loadCatalogRelationMap(connName, actualTableName.getCatalog());

        TableRelationTree top = new TableRelationTree(actualTableName);
        findTableSuperTypes(relations,top);
        return top;
    }

    private void findTableSuperTypes(Set<TableRelationDto> relations, TableRelationTree parent) {
        ActualTableName originTable = parent.getTableName();
        for (TableRelationDto relation : relations) {
            ActualTableName targetTableName = relation.getTargetTableName();
            if(targetTableName.equals(originTable)){
                TableRelationDto.Relation currentRelation = TableRelationDto.Relation.valueOf(relation.getRelation());
                TableRelationTree subParent = new TableRelationTree(relation.getSourceColumnName(),relation.getSourceTableName(),relation.getSourceColumnName(),currentRelation);
                parent.addRelation(subParent);
                findTableSuperTypes(relations,subParent);
            }
        }
    }

    static class ColumnPropertyFilter implements PropertyFilter {
        private String [] filterColumns = {"columnType","comments","primaryKey"};
        @Override
        public boolean apply(Object object, String name, Object value) {
            if(object != null && object instanceof Column){
                return !ArrayUtils.contains(filterColumns,name);
            }
            return true;
        }
    }
    private final static ColumnPropertyFilter columnPropertyFilter =  new ColumnPropertyFilter();

    /**
     * 序列化现在的所有表关系
     */
    public void serializable(){
        try {
            fileManager.writeConfig(JdbcService.MODULE,"metadata/relations",JSON.toJSONString(tableRelationDtoMap));
        } catch (IOException e) {
            LogUtil.error("table relation serializable error : {}",e.getMessage(),e);
        }
    }

    /**
     * 加载表关系
     */
    @PostConstruct
    public void init(){
        try {
            String readFileToString = fileManager.readConfig(JdbcService.MODULE, "metadata/relations");
            if(StringUtils.isBlank(readFileToString)){
                return ;
            }
            // 简单加载,不获取数据表的完整信息
            TypeReference<Map<String, Map<String, Set<TableRelationDto>>>> mapTypeReference = new TypeReference<Map<String, Map<String, Set<TableRelationDto>>>>() {};
            tableRelationDtoMap = JSON.parseObject(readFileToString, mapTypeReference);
        } catch (IOException e) {
	        LogUtil.error("加载表关系失败:{}",e.getMessage());
        }

    }

    private Set<TableRelationDto> loadCatalogRelationMap(String connName, String catalog) {
        Map<String, Set<TableRelationDto>> catalogRelationMap = tableRelationDtoMap.computeIfAbsent(connName, k -> new HashMap<String, Set<TableRelationDto>>());
        Set<TableRelationDto> tableRelationDtos = catalogRelationMap.computeIfAbsent(catalog, k -> new HashSet<>());
        return tableRelationDtos;
    }


}
