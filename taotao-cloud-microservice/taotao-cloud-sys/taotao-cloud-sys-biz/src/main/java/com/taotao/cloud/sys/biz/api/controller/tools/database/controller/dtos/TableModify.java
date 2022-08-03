package com.taotao.cloud.sys.biz.api.controller.tools.database.controller.dtos;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.compare.DiffType;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.compare.ModifyColumn;
import com.taotao.cloud.sys.biz.api.controller.tools.database.service.dtos.compare.ModifyIndex;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 单个表的变更信息
 */
@Data
public class TableModify {
    private String tableName;
    private DiffType diffType;
    private TableMetaData newTable;
    private List<ModifyColumn> modifyColumns = new ArrayList<>();
    private List<ModifyIndex> modifyIndices = new ArrayList<>();

    public TableModify() {
    }

    public TableModify(String tableName, DiffType diffType) {
        this.tableName = tableName;
        this.diffType = diffType;
    }

    public TableModify(String tableName, DiffType diffType, TableMetaData newTable) {
        this.tableName = tableName;
        this.diffType = diffType;
        this.newTable = newTable;
    }

}
