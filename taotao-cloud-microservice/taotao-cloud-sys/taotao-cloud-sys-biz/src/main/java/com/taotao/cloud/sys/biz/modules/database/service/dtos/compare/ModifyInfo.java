package com.taotao.cloud.sys.biz.modules.database.service.dtos.compare;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public final class ModifyInfo {
	private List<ModifyTable> modifyTables = new ArrayList<>();
	private List<ModifyColumn> modifyColumns = new ArrayList<>();
	private List<ModifyIndex> modifyIndices = new ArrayList<>();
	private String dbType;

	public void addModifyTable(ModifyTable modifyTable) {
		modifyTables.add(modifyTable);
	}

	public void addModifyColumn(ModifyColumn modifyColumn) {
		modifyColumns.add(modifyColumn);
	}

	public void addModifyIndex(ModifyIndex modifyIndex) {
		modifyIndices.add(modifyIndex);
	}
}
