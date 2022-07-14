package com.taotao.cloud.workflow.biz.form.model.supplementcard;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 补卡申请
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class SupplementCardInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "员工姓名")
    private String fullName;
    @ApiModelProperty(value = "流程主键")
    private String description;
    @ApiModelProperty(value = "证明人")
    private String witness;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "开始时间")
    private Long  startTime;
    @ApiModelProperty(value = "所在职务")
    private String position;
    @ApiModelProperty(value = "补卡次数")
    private String supplementNum;
    @ApiModelProperty(value = "结束时间")
    private Long  endTime;
    @ApiModelProperty(value = "申请日期")
    private Long  applyDate;
    @ApiModelProperty(value = "所在部门")
    private String department;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;

}
