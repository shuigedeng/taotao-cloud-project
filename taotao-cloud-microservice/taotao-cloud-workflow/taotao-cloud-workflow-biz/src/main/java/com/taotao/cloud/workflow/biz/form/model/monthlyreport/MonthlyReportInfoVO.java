package com.taotao.cloud.workflow.biz.form.model.monthlyreport;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 月工作总结
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class MonthlyReportInfoVO {
    @ApiModelProperty(value = "主键id")
    private String id;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "次月日期")
    private Long  nPFinishTime;
    @ApiModelProperty(value = "次月目标")
    private String nFinishMethod;
    @ApiModelProperty(value = "所属职务")
    private String applyPost;
    @ApiModelProperty(value = "总体评价")
    private String overalEvaluat;
    @ApiModelProperty(value = "创建人")
    private String applyUser;
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "所属部门")
    private String applyDept;
    @ApiModelProperty(value = "工作事项")
    private String nPWorkMatter;
    @ApiModelProperty(value = "完成时间")
    private Long  planEndTime;
    @ApiModelProperty(value = "创建日期")
    private Long  applyDate;
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @ApiModelProperty(value = "流程单据")
    private String billNo;
}
