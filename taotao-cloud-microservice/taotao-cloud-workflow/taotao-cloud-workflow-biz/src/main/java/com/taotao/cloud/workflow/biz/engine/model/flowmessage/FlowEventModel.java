package com.taotao.cloud.workflow.biz.engine.model.flowmessage;

import jnpf.engine.entity.FlowTaskOperatorRecordEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：JNPF开发平台组
 * @version: V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date ：2022/3/31 16:15
 */
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
