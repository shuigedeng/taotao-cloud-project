package com.taotao.cloud.sys.biz.api.controller.tools.database.controller;

import com.taotao.cloud.sys.biz.api.controller.tools.database.controller.dtos.BatchTableRelationParam;
import com.taotao.cloud.sys.biz.api.controller.tools.database.controller.dtos.ConfigTableRelationFromSql;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.JdbcMetaService;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.RelationSql;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableMark;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableRelation;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableRelationTree;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.TabeRelationMetaData;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.TableMarkMetaData;
import net.sf.jsqlparser.JSQLParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 扩展表元数据,表关系,表级别
 */
@RestController
@RequestMapping("/db/metadata/extend")
@Validated
public class ExtendMetadataController {
    @Autowired
    private TabeRelationMetaData tableRelationService;
    @Autowired
    private JdbcMetaService jdbcService;
    @Autowired
    private TableMarkMetaData tableMarkService;

    /**
     * 可用的标签
     * @return
     */
    @GetMapping("/mark/tags")
    public String [] tags(){
//        return TableMark.supportTags;
        return TableMark.supportTags;
    }

    /**
     * 配置表标记,将某个表配置为字典表,业务表,系统表,统计表等
     * @param tableMarks
     */
    @PostMapping("/mark/config/{connName}/tableMark")
    public void configTableMark(@PathVariable("connName") String connName,@RequestBody @Valid Set<TableMark> tableMarks){
        tableMarkService.configTableMark(connName,tableMarks);
    }

    /**
     * 查询某张表的表标记
     * @param connName
     * @param actualTableName
     * @return
     */
    @GetMapping("/mark/tableTags")
    public Set<String> tableTags(@NotNull String connName, ActualTableName actualTableName){
        TableMark tableMark = tableMarkService.getTableMark(connName,actualTableName);
        return tableMark.getTags();
    }

    /**
     * 批量配置表关系
     * @param batchTableRelationParam
     */
    @PostMapping("/relation/config")
    public void configBatch(@RequestBody @Valid BatchTableRelationParam batchTableRelationParam){
        final String connName = batchTableRelationParam.getConnName();
        tableRelationService.configRelation(connName,batchTableRelationParam.getNamespace(),batchTableRelationParam.getTableRelations());
    }

    /**
     * 通过 sql 来配置表关系
     * @param configTableRelationFromSql
     * @throws JSQLParserException
     */
    @PostMapping("/relation/config/fromSql")
    public void configBatchFromSql(@RequestBody @Valid ConfigTableRelationFromSql configTableRelationFromSql) throws JSQLParserException {
        final String connName = configTableRelationFromSql.getConnName();
        final Namespace namespace = configTableRelationFromSql.getNamespace();
        final List<RelationSql> relationSqls = configTableRelationFromSql.getRelationSqls();
        tableRelationService.configRelationFromSqls(connName,namespace,relationSqls);
    }

    /**
     * 查询当前名称空间所有表关系
     * @param connName 连接名
     * @param namespace 名称空间
     * @return
     */
    @GetMapping("/relation/list")
    public Set<TableRelation> relations(@NotNull String connName, Namespace namespace){
        return tableRelationService.tableRelations(connName, namespace);
    }

    /**
     * 当前表被哪些表引用
     * @param connName
     * @param catalog
     * @param tableName
     * @return
     */
    @GetMapping("/relation/parents")
    public List<TableRelation> parents(@NotNull String connName, @Validated ActualTableName actualTableName){
        return tableRelationService.parents(connName,actualTableName);
    }

    /**
     * 当前表引用的表
     * @param connName
     * @param catalog
     * @param tableName
     * @return
     */
    @GetMapping("/relation/childs")
    public List<TableRelation> childs(@NotNull String connName, @Validated ActualTableName actualTableName){
        return tableRelationService.childs(connName,actualTableName);
    }

    /**
     * 找到当前表引用的表层级关系
     * @param connName
     * @param catalog
     * @param tableName
     * @return
     */
    @GetMapping("/relation/hierarchy")
    public TableRelationTree hierarchy(@NotNull String connName, @Validated ActualTableName actualTableName){
        return tableRelationService.hierarchy(connName,actualTableName);
    }

    /**
     * 当前表被引用表层级关系
     * @param connName
     * @param actualTableName
     * @return
     */
    @GetMapping("/relation/superTypes")
    public TableRelationTree superTypes(@NotNull String connName, @Validated ActualTableName actualTableName){
        return tableRelationService.superTypes(connName,actualTableName);
    }
}
