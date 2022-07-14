package com.taotao.cloud.workflow.biz.form.model.monthlyreport;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
public class MonthlyReportForm {
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "次月日期")
    private String nPFinishTime;
    @ApiModelProperty(value = "次月目标")
    private String nFinishMethod;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "所属职务")
    private String applyPost;
    @ApiModelProperty(value = "总体评价")
    private String overalEvaluat;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "创建人")
    private String applyUser;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "所属部门")
    private String applyDept;
    @ApiModelProperty(value = "工作事项")
    private String nPWorkMatter;
    @ApiModelProperty(value = "完成时间")
    private Long  planEndTime;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "创建日期")
    private Long  applyDate;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
