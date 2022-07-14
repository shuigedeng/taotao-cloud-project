package com.taotao.cloud.workflow.biz.engine.model.flowbefore;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 *
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 9:18
 */
@Data
public class FlowSummary {
    @ApiModelProperty(value = "主键")
    private String id;
    @ApiModelProperty(value = "名称")
    private String fullName;
    @ApiModelProperty(value = "意见")
    private String handleOpinion;
    @ApiModelProperty(value = "用户")
    private String userName;
    @ApiModelProperty(value = "时间")
    private Long handleTime;
    @ApiModelProperty(value = "状态")
    private Integer handleStatus;
    @ApiModelProperty(value = "流转操作人")
    private String operatorId;
    @ApiModelProperty(value = "子流程")
    private List<FlowSummary> list;
}
