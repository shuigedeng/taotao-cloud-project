package com.taotao.cloud.workflow.api.common.model.engine.flowengine;

import java.util.List;

import lombok.Data;

/**
 *
 */
@Data
public class FlowExportModel {

    private FlowEngineEntity flowEngine;

    private List<FlowEngineVisibleEntity> visibleList;

}
