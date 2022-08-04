package com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.compare;

import com.taotao.cloud.sys.biz.api.controller.tools.database.service.meta.dtos.Index;
import lombok.Data;

/**
     * 索引修改
     */
@Data
public final class ModifyIndex {
	private String tableName;
	private DiffType diffType;
	private Index baseIndex;
	private Index newIndex;

	public ModifyIndex(String tableName, DiffType diffType, Index baseIndex, Index newIndex) {
		this.tableName = tableName;
		this.diffType = diffType;
		this.baseIndex = baseIndex;
		this.newIndex = newIndex;
	}
}
