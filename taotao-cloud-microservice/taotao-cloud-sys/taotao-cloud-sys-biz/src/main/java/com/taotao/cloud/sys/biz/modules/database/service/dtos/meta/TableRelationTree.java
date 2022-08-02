package com.taotao.cloud.sys.biz.modules.database.service.dtos.meta;

import com.sanri.tools.modules.database.service.meta.dtos.ActualTableName;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TableRelationTree {
    private String tableName;
    private List<TableRelationTree> relationTrees = new ArrayList<>();
    private String originColumn;
    private String column;
    private TableRelation.RelationEnum relation;

    public TableRelationTree(String tableName) {
        this.tableName = tableName;
    }

    public TableRelationTree(String originColumn, String tableName, String column, TableRelation.RelationEnum relation) {
        this.tableName = tableName;
        this.originColumn = originColumn;
        this.column = column;
        this.relation = relation;
    }

    public String getId(){
        return tableName;
    }

    public String getLabel(){
        if (StringUtils.isBlank(originColumn) && relation == null && StringUtils.isBlank(column)){
            return tableName + " | root";
        }
        return tableName + " | " + originColumn +" "+ relation +" "+ column;
    }

    public List<TableRelationTree> getChildren(){
        return relationTrees;
    }

    public String getTableName() {
        return tableName;
    }

    public void addRelation(TableRelationTree tableRelationTree){
        relationTrees.add(tableRelationTree);
    }

    public TableRelation.RelationEnum getRelation() {
        return relation;
    }

    public String getColumn() {
        return column;
    }

    public String getOriginColumn() {
        return originColumn;
    }
}
