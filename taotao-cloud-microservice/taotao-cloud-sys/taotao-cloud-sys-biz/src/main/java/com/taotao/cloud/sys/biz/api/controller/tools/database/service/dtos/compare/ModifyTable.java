package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.compare;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.meta.TableMetaData;
import lombok.Data;

/**
     * 数据表增删
     */
@Data
public class ModifyTable {
	private String tableName;
	private DiffType diffType;
	private TableMetaData tableMetaData;

	public ModifyTable(String tableName, DiffType diffType) {
		this.tableName = tableName;
		this.diffType = diffType;
	}

	public ModifyTable(String tableName, DiffType diffType, TableMetaData tableMetaData) {
		this.tableName = tableName;
		this.diffType = diffType;
		this.tableMetaData = tableMetaData;
	}
}
