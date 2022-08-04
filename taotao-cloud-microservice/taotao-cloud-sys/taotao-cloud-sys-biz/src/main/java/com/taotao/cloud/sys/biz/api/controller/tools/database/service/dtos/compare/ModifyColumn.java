package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.compare;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Column;
import lombok.Data;

/**
     * 列修改
     */
@Data
public final class ModifyColumn {
	private String tableName;
	private DiffType diffType;
	private Column baseColumn;
	private Column newColumn;

	public ModifyColumn(String tableName, DiffType diffType, Column baseColumn, Column newColumn) {
		this.tableName = tableName;
		this.diffType = diffType;
		this.baseColumn = baseColumn;
		this.newColumn = newColumn;
	}
}
