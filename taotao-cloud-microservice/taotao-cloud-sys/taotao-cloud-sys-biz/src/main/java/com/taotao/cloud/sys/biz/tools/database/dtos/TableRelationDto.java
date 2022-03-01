package com.taotao.cloud.sys.biz.tools.database.dtos;


import com.taotao.cloud.sys.biz.tools.core.validation.custom.EnumStringValue;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 表关系
 */
public class TableRelationDto {
    @Valid
    private ActualTableName sourceTableName;
    @Valid
    private ActualTableName targetTableName;
    @NotNull
    private String sourceColumnName;
    @NotNull
    private String targetColumnName;

    // ONE_ONE,ONE_MANY,MANY_MANY
    @NotNull
    @EnumStringValue({"ONE_ONE","ONE_MANY","MANY_MANY"})
    private String relation;

    public static enum Relation{
        ONE_ONE,ONE_MANY,MANY_MANY
    }

	public ActualTableName getSourceTableName() {
		return sourceTableName;
	}

	public void setSourceTableName(
		ActualTableName sourceTableName) {
		this.sourceTableName = sourceTableName;
	}

	public ActualTableName getTargetTableName() {
		return targetTableName;
	}

	public void setTargetTableName(
		ActualTableName targetTableName) {
		this.targetTableName = targetTableName;
	}

	public String getSourceColumnName() {
		return sourceColumnName;
	}

	public void setSourceColumnName(String sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}

	public String getTargetColumnName() {
		return targetColumnName;
	}

	public void setTargetColumnName(String targetColumnName) {
		this.targetColumnName = targetColumnName;
	}

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}
}
