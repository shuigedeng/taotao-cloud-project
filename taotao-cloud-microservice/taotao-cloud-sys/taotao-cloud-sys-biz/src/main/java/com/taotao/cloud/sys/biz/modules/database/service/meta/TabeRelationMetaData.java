package com.taotao.cloud.sys.biz.modules.database.service.meta;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.PropertyFilter;
import com.sanri.tools.modules.core.service.file.FileManager;
import com.sanri.tools.modules.database.service.dtos.meta.RelationSql;
import com.sanri.tools.modules.database.service.dtos.meta.TableRelation;
import com.sanri.tools.modules.database.service.dtos.meta.TableRelationTree;
import com.sanri.tools.modules.database.service.meta.dtos.ActualTableName;
import com.sanri.tools.modules.database.service.meta.dtos.Column;
import com.sanri.tools.modules.database.service.meta.dtos.Namespace;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TabeRelationMetaData {
    @Autowired
    private FileManager fileManager;

    /**
     * 表关系图 connName => Namespace => 表关系列表
     */
    private Map<String,Map<Namespace, Set<TableRelation>>> tableRelationMap = new ConcurrentHashMap<>();

    private CCJSqlParserManager parserManager = new CCJSqlParserManager();

    /**
     * 加载指定名称空间的表关系
     * @param connName
     * @param namespace
     * @return
     */
    public Set<TableRelation> tableRelations(String connName, Namespace namespace){
        return loadNamespaceRelationMap(connName,namespace);
    }

    /**
     * 新增关系
     * @param connName
     * @param catalog
     * @param TableRelationDtos
     */
    public void configRelation(String connName, Namespace namespace, Set<TableRelation> TableRelationDtos){
        Set<TableRelation> relations = loadNamespaceRelationMap(connName, namespace);
        relations.addAll(TableRelationDtos);

        serializable(connName,namespace);
    }

    /**
     * 通过 sql 来新增关系
     * @param connName
     * @param namespace
     * @param sqls
     */
    public void configRelationFromSqls(String connName,Namespace namespace,List<RelationSql> relationSqls) throws JSQLParserException {
        Set<TableRelation> tableRelations = new HashSet<>();
        for (RelationSql relationSql : relationSqls) {
            Map<String,String> tableAlaisMap = new HashMap<>();
            Select select = (Select) parserManager.parse(new StringReader(relationSql.getSql()));
            final PlainSelect selectBody = (PlainSelect) select.getSelectBody();
            final Table fromItem = (Table) selectBody.getFromItem();
            if (fromItem.getAlias() != null) {
                tableAlaisMap.put(fromItem.getAlias().getName(), fromItem.getName());
            }
            tableAlaisMap.put(fromItem.getName(), fromItem.getName());
            final List<Join> joins = selectBody.getJoins();
            for (Join join : joins) {
                final Table rightItem = (Table) join.getRightItem();
                if (rightItem.getAlias() != null) {
                    tableAlaisMap.put(rightItem.getAlias().getName(), rightItem.getName());
                }
                tableAlaisMap.put(rightItem.getName(),rightItem.getName());
                final EqualsTo equalsTo = (EqualsTo) join.getOnExpression();
                final net.sf.jsqlparser.schema.Column leftExpression = (net.sf.jsqlparser.schema.Column) equalsTo.getLeftExpression();
                final net.sf.jsqlparser.schema.Column rightExpression = (net.sf.jsqlparser.schema.Column) equalsTo.getRightExpression();

                String sourceTableName = tableAlaisMap.get(leftExpression.getTable().getName().toUpperCase());
                String targetTableName = tableAlaisMap.get(rightExpression.getTable().getName().toUpperCase());
                String sourceColumnName = leftExpression.getColumnName();
                String targetColumnName = rightExpression.getColumnName();

                final TableRelation tableRelation = new TableRelation(sourceTableName, targetTableName, sourceColumnName, targetColumnName, relationSql.getRelation().name());
                tableRelations.add(tableRelation);
            }
        }

        log.info("通过 sql 语句为 {} {} 新加了 {} 条表关系",connName,namespace,tableRelations.size());

        final Map<Namespace, Set<TableRelation>> namespaceSetMap = tableRelationMap.computeIfAbsent(connName, k -> new HashMap<>());
        final Set<TableRelation> tableRelationSet = namespaceSetMap.computeIfAbsent(namespace, k -> new HashSet<>());
        tableRelationSet.addAll(tableRelations);

        serializable(connName,namespace);
    }

    /**
     * 删除关系
     * @param connName
     * @param catalog
     * @param TableRelationDtos
     */
    public void drop(String connName,Namespace namespace,Set<TableRelation> TableRelationDtos){
        Set<TableRelation> relations = loadNamespaceRelationMap(connName, namespace);
        relations.removeAll(TableRelationDtos);

        serializable(connName,namespace);
    }

    /**
     * 查询某个表使用其它表的表关系
     * @param connName
     * @param catalog
     * @param tableName
     * @return
     */
    public List<TableRelation> childs(String connName, ActualTableName actualTableName){
        Set<TableRelation> relations = loadNamespaceRelationMap(connName, actualTableName.getNamespace());
        return relations.stream().filter(relation -> relation.getSourceTableName().equalsIgnoreCase(actualTableName.getTableName())).collect(Collectors.toList());
    }

    /**
     * 查询使用某个表的表关系
     * @param connName
     * @param catalog
     * @param tableName
     * @return
     */
    
    public List<TableRelation> parents(String connName, ActualTableName actualTableName){
        Set<TableRelation> relations = loadNamespaceRelationMap(connName, actualTableName.getNamespace());
        Predicate<TableRelation> function = relation -> relation.getTargetTableName().equalsIgnoreCase(actualTableName.getTableName())
                || TableRelation.RelationEnum.valueOf(relation.getRelation()) != TableRelation.RelationEnum.ONE_MANY;
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
        Set<TableRelation> relations = loadNamespaceRelationMap(connName, actualTableName.getNamespace());

        TableRelationTree top = new TableRelationTree(actualTableName.getTableName());
        // TableRelation => 访问次数
        Map<TableRelation,Integer> track = new HashMap<>();
        findTableHierarchy(relations,top,track);
        return top;
    }

    private void findTableHierarchy(Set<TableRelation> relations, TableRelationTree parent,Map<TableRelation,Integer> track) {
        String originTableName = parent.getTableName();
        for (TableRelation relation : relations) {
            String sourceTableName = relation.getSourceTableName();
            if(sourceTableName.equalsIgnoreCase(originTableName)){
                if (track.containsKey(relation)){
                    log.info("已存在足迹 {} , 跳出递归",relation);
                    return ;
                }
                final Integer integer = track.computeIfAbsent(relation, k -> 0);
                track.put(relation,integer.intValue() + 1);

                TableRelation.RelationEnum currentRelation = TableRelation.RelationEnum.valueOf(relation.getRelation());
                TableRelationTree subParent = new TableRelationTree(relation.getSourceColumnName(),relation.getTargetTableName(),relation.getTargetColumnName(),currentRelation);
                parent.addRelation(subParent);
                findTableHierarchy(relations,subParent,track);
            }
        }
    }

    public TableRelationTree superTypes(String connName, ActualTableName actualTableName) {
        Set<TableRelation> relations = loadNamespaceRelationMap(connName, actualTableName.getNamespace());

        TableRelationTree top = new TableRelationTree(actualTableName.getTableName());
        findTableSuperTypes(relations,top);
        return top;
    }

    private void findTableSuperTypes(Set<TableRelation> relations, TableRelationTree parent) {
        String originTableName = parent.getTableName();
        for (TableRelation relation : relations) {
            String targetTableName = relation.getTargetTableName();
            if(targetTableName.equalsIgnoreCase(originTableName)){
                TableRelation.RelationEnum currentRelation = TableRelation.RelationEnum.valueOf(relation.getRelation());
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
     * 序列化指定连接, 指定命名空间的表关系
     */
    public void serializable(String connName,Namespace namespace){
        try {
            final File relationDir = fileManager.mkConfigDir("database/metadata/relations");
            final File relationFile = new File(relationDir, connName + "/" + namespace.toNamespace());
            final Map<Namespace, Set<TableRelation>> namespaceSetMap = tableRelationMap.computeIfAbsent(connName,k-> new HashMap<>());
            final Set<TableRelation> tableRelations = namespaceSetMap.computeIfAbsent(namespace, k-> new HashSet<TableRelation>());
            FileUtils.writeStringToFile(relationFile,JSON.toJSONString(tableRelations),StandardCharsets.UTF_8);
        } catch (IOException e) {
            log.error("table relation serializable error : {}",e.getMessage(),e);
        }
    }

    /**
     * 加载表关系
     * $configDir[Dir]
     *   metadata/relations[Dir]
     *     $connection[Dir]
     *       $namespace[File]
     *
     */
    @PostConstruct
    public void init(){
        try {
            final File relationDir = fileManager.mkConfigDir("database/metadata/relations");
            for (File file : relationDir.listFiles()) {
                final String conneciton = file.getName();
                final Map<Namespace, Set<TableRelation>> namespaceSetMap = tableRelationMap.computeIfAbsent(conneciton, k ->new HashMap<>());

                final File[] relationFiles = file.listFiles();
                for (File relationFile : relationFiles) {
                    final String name = relationFile.getName();
                    final String[] catalogAndSchema = StringUtils.splitPreserveAllTokens(name, "@");
                    for (int i = 0; i < catalogAndSchema.length; i++) {
                        catalogAndSchema[i] = StringUtils.isBlank(catalogAndSchema[i]) ? null : catalogAndSchema[i];
                    }
                    final Namespace namespace = new Namespace(catalogAndSchema[0], catalogAndSchema[1]);

                    final String relations = FileUtils.readFileToString(relationFile, StandardCharsets.UTF_8);
                    final Set<TableRelation> tableRelations = JSON.parseObject(relations, new TypeReference<Set<TableRelation>>() {});
                    namespaceSetMap.putIfAbsent(namespace,tableRelations);
                }

            }
        } catch (IOException e) {
            log.error("加载表关系失败:{}",e.getMessage());
        }

        System.out.println();
    }

    private Set<TableRelation> loadNamespaceRelationMap(String connName, Namespace namespace) {
        Map<Namespace, Set<TableRelation>> catalogRelationMap = tableRelationMap.computeIfAbsent(connName, k -> new HashMap<Namespace, Set<TableRelation>>());
        Set<TableRelation> tableRelationDtos = catalogRelationMap.computeIfAbsent(namespace, k -> new HashSet<>());
        return tableRelationDtos;
    }

}
