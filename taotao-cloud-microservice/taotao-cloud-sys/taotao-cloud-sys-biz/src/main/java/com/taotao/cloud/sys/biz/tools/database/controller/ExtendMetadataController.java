package com.taotao.cloud.sys.biz.tools.database.controller;

import com.taotao.cloud.sys.biz.tools.database.dtos.BatchTableRelationParam;
import com.taotao.cloud.sys.biz.tools.database.dtos.TableMark;
import com.taotao.cloud.sys.biz.tools.database.dtos.TableRelationDto;
import com.taotao.cloud.sys.biz.tools.database.dtos.TableRelationTree;
import com.taotao.cloud.sys.biz.tools.database.service.JdbcService;
import com.taotao.cloud.sys.biz.tools.database.service.TableMarkService;
import com.taotao.cloud.sys.biz.tools.database.service.TableRelationService;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
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
    private TableRelationService tableRelationService;
    @Autowired
    private JdbcService jdbcService;
    @Autowired
    private TableMarkService tableMarkService;

    /**
     * 可用的标签
     * @return
     */
    @GetMapping("/mark/tags")
    public List<String> tags(){
        return Arrays.asList("biz","dict","sys","report","biz_config","deprecated");
    }

    /**
     * 配置表标记,将某个表配置为字典表,业务表,系统表,统计表等
     * @param tableMarks
     */
    @PostMapping("/mark/config/tableMark")
    public void configTableMark(@RequestBody @Valid Set<TableMark> tableMarks){
        tableMarkService.configTableMark(tableMarks);
    }

    @GetMapping("/mark/tableTags")
    public Set<String> tableTags(@NotNull String connName, String catalog, String schema, @NotNull String tableName){
        ActualTableName actualTableName = new ActualTableName(catalog, schema, tableName);
        TableMark tableMark = tableMarkService.getTableMark(connName,actualTableName);
        return tableMark.getTags();
    }

    /**
     * 批量配置表关系
     * @param batchTableRelationParam
     */
    @PostMapping("/relation/config")
    public void configBatch(@RequestBody @Valid BatchTableRelationParam batchTableRelationParam){
        String connName = batchTableRelationParam.getConnName();
        String catalog = batchTableRelationParam.getCatalog();

        tableRelationService.configRelation(connName,catalog,batchTableRelationParam.getTableRelations());
    }

    /**
     * 当前表被哪些表引用
     * @param connName
     * @param catalog
     * @param tableName
     * @return
     */
    @GetMapping("/relation/parents")
    public List<TableRelationDto> parents(@NotNull String connName, String catalog,String schema, @NotNull String tableName){
        ActualTableName actualTableName = new ActualTableName(catalog, schema, tableName);
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
    public List<TableRelationDto> childs(@NotNull String connName, String catalog,String schema, @NotNull String tableName){
        ActualTableName actualTableName = new ActualTableName(catalog, schema, tableName);
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
    public TableRelationTree hierarchy(@NotNull String connName, String catalog, String schema,@NotNull String tableName){
        ActualTableName actualTableName = new ActualTableName(catalog, schema, tableName);
        return tableRelationService.hierarchy(connName,actualTableName);
    }

    @GetMapping("/relation/superTypes")
    public TableRelationTree superTypes(@NotNull String connName, String catalog, String schema,@NotNull String tableName){
        ActualTableName actualTableName = new ActualTableName(catalog, schema, tableName);
        return tableRelationService.superTypes(connName,actualTableName);
    }
}
