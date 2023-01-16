package com.taotao.cloud.workflow.api.common.model.engine.flowengine;

import java.util.List;
import lombok.Data;

/**
 *
 */
@Data
public class FlowEngineListSelectVO {

    private String id;
    private String fullName;
    private Boolean hasChildren;
    private List<FlowEngineListSelectVO> children;
}
