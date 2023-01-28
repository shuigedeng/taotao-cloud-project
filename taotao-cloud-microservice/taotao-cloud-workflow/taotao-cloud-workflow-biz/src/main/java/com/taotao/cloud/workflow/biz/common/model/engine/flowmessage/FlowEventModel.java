package com.taotao.cloud.workflow.biz.common.model.engine.flowmessage;

import com.taotao.cloud.workflow.biz.engine.entity.FlowTaskOperatorRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlowEventModel {

    //数据
    private String dataJson;
    //系统匹配
    private String relationField;
    //操作对象
    private FlowTaskOperatorRecordEntity record;

}
