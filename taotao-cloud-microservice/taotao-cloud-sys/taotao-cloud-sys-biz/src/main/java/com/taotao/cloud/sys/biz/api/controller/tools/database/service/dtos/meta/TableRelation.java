package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta;

import com.taotao.cloud.sys.biz.api.controller.tools.core.validation.custom.EnumStringValue;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 表关系
 */
@Getter
@Setter
public class TableRelation {
    private String sourceTableName;
    private String targetTableName;
    private String sourceColumnName;
    private String targetColumnName;

    @NotNull
    @EnumStringValue({"ONE_ONE","ONE_MANY","MANY_MANY"})
    private String relation;

    public TableRelation() {
    }

    public TableRelation(String sourceTableName, String targetTableName, String sourceColumnName, String targetColumnName, @NotNull String relation) {
        this.sourceTableName = sourceTableName;
        this.targetTableName = targetTableName;
        this.sourceColumnName = sourceColumnName;
        this.targetColumnName = targetColumnName;
        this.relation = relation;
    }

    public static enum RelationEnum {
        ONE_ONE,ONE_MANY,MANY_MANY
    }

    /**
     * 获取反转对象
     * @return
     */
    public TableRelation reverse(){
        return new TableRelation(targetTableName,sourceTableName,targetColumnName,sourceColumnName,relation);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TableRelation that = (TableRelation) o;

        if (sourceTableName != null ? !sourceTableName.equalsIgnoreCase(that.sourceTableName) : that.sourceTableName != null) {
            return false;
        }
        if (targetTableName != null ? !targetTableName.equalsIgnoreCase(that.targetTableName) : that.targetTableName != null) {
            return false;
        }
        if (sourceColumnName != null ? !sourceColumnName.equalsIgnoreCase(that.sourceColumnName) : that.sourceColumnName != null) {
            return false;
        }
        if (targetColumnName != null ? !targetColumnName.equalsIgnoreCase(that.targetColumnName) : that.targetColumnName != null) {
            return false;
        }
//        if (relation != null ? !relation.equals(that.relation) : that.relation != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = sourceTableName != null ? sourceTableName.toUpperCase().hashCode() : 0;
        result = 31 * result + (targetTableName != null ? targetTableName.toUpperCase().hashCode() : 0);
        result = 31 * result + (sourceColumnName != null ? sourceColumnName.toUpperCase().hashCode() : 0);
        result = 31 * result + (targetColumnName != null ? targetColumnName.toUpperCase().hashCode() : 0);
//        result = 31 * result + (relation != null ? relation.hashCode() : 0);
        return result;
    }
}
