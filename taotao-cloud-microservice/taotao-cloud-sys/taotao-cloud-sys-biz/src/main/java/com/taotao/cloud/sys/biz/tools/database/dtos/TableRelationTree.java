package com.taotao.cloud.sys.biz.tools.database.dtos;

import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class TableRelationTree {
    private ActualTableName tableName;
    private List<TableRelationTree> relationTrees = new ArrayList<>();
    private String originColumn;
    private String column;
    private TableRelationDto.Relation relation;

    public TableRelationTree(ActualTableName tableName) {
        this.tableName = tableName;
    }

    public TableRelationTree(String originColumn,ActualTableName tableName, String column, TableRelationDto.Relation relation) {
        this.tableName = tableName;
        this.originColumn = originColumn;
        this.column = column;
        this.relation = relation;
    }

    public String getId(){
        if (StringUtils.isBlank(column)){
            return tableName.getFullName();
        }
        return tableName.getFullName() + "." + column;
    }

    public String getLabel(){
        if (StringUtils.isBlank(column)){
            return tableName.getFullName();
        }
        return tableName.getFullName() + " | " + originColumn +" "+ relation +" "+ column;
    }

    public List<TableRelationTree> getChildren(){
        return relationTrees;
    }

    public ActualTableName getTableName() {
        return tableName;
    }

    public void addRelation(TableRelationTree tableRelationTree){
        relationTrees.add(tableRelationTree);
    }

    public TableRelationDto.Relation getRelation() {
        return relation;
    }

    public String getColumn() {
        return column;
    }

    public String getOriginColumn() {
        return originColumn;
    }
}
