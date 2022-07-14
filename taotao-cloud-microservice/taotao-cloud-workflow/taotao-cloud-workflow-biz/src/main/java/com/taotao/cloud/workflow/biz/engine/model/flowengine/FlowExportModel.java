package com.taotao.cloud.workflow.biz.engine.model.flowengine;

import java.util.List;

import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineEntity;
import com.taotao.cloud.workflow.biz.engine.entity.FlowEngineVisibleEntity;
import lombok.Data;

/**
 *
 */
@Data
public class FlowExportModel {

    private FlowEngineEntity flowEngine;

    private List<FlowEngineVisibleEntity> visibleList;

}
