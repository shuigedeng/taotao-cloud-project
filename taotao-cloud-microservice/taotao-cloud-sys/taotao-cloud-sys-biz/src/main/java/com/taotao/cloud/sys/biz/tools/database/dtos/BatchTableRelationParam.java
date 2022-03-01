package com.taotao.cloud.sys.biz.tools.database.dtos;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

public class BatchTableRelationParam {
    @NotNull
    private String connName;
    private String catalog;

    @Valid
    Set<TableRelationDto> tableRelations = new HashSet<>();

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public Set<TableRelationDto> getTableRelations() {
		return tableRelations;
	}

	public void setTableRelations(
		Set<TableRelationDto> tableRelations) {
		this.tableRelations = tableRelations;
	}
}
