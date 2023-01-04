package com.taotao.cloud.workflow.api.common.model.engine.flowtask;

import java.util.List;
import lombok.Data;

/**
 *
 *
 */
@Data
public class FlowTableModel {

    private String relationField;
    private String relationTable;
    private String table;
    private String tableName;
    private String tableField;
    private String typeId;
    private List<FlowFieldsModel> fields;

}
