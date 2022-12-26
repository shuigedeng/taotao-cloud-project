package com.taotao.cloud.workflow.biz.form.model.quotationapproval;


import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 报价审批表
 *
 */
@Data
public class QuotationApprovalForm {
    @Schema(description = "相关附件")
    private String fileJson;
    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "合作人名")
    private String partnerName;
    @NotNull(message = "填表日期不能为空")
    @Schema(description = "填表日期")
    private Long  writeDate;
    @Schema(description = "情况描述")
    private String custSituation;
    @Schema(description = "填报人")
    private String writer;
    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "类型")
    private String quotationType;
    @Schema(description = "客户名称")
    private String customerName;
    @Schema(description = "模板参考")
    private String standardFile;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;

}
