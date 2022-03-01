package com.taotao.cloud.sys.biz.tools.database.controller.dtos;


import com.taotao.cloud.sys.biz.tools.database.service.MetaCompareService;
import com.taotao.cloud.sys.biz.tools.database.service.MetaCompareService.DiffType;
import com.taotao.cloud.sys.biz.tools.database.service.MetaCompareService.ModifyColumn;
import com.taotao.cloud.sys.biz.tools.database.service.MetaCompareService.ModifyIndex;
import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.TableMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * 单个表的变更信息
 */
public class TableModify {
    private String tableName;
    private MetaCompareService.DiffType diffType;
    private TableMetaData newTable;
    private List<MetaCompareService.ModifyColumn> modifyColumns = new ArrayList<>();
    private List<MetaCompareService.ModifyIndex> modifyIndices = new ArrayList<>();

    public TableModify() {
    }

    public TableModify(String tableName, MetaCompareService.DiffType diffType) {
        this.tableName = tableName;
        this.diffType = diffType;
    }

    public TableModify(String tableName, MetaCompareService.DiffType diffType, TableMetaData newTable) {
        this.tableName = tableName;
        this.diffType = diffType;
        this.newTable = newTable;
    }

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public DiffType getDiffType() {
		return diffType;
	}

	public void setDiffType(
		DiffType diffType) {
		this.diffType = diffType;
	}

	public TableMetaData getNewTable() {
		return newTable;
	}

	public void setNewTable(TableMetaData newTable) {
		this.newTable = newTable;
	}

	public List<ModifyColumn> getModifyColumns() {
		return modifyColumns;
	}

	public void setModifyColumns(
		List<ModifyColumn> modifyColumns) {
		this.modifyColumns = modifyColumns;
	}

	public List<ModifyIndex> getModifyIndices() {
		return modifyIndices;
	}

	public void setModifyIndices(
		List<ModifyIndex> modifyIndices) {
		this.modifyIndices = modifyIndices;
	}
}
