package com.taotao.cloud.workflow.biz.common.model.form.workcontactsheet;


import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 工作联系单
 */
@Data
public class WorkContactSheetForm {
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "收件部门")
    private String serviceDepartment;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "收件人")
    private String recipients;
    @NotNull(message = "必填")
    @Schema(description = "发件日期")
    private Long toDate;
    @Schema(description = "发件人")
    private String drawPeople;
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @NotNull(message = "必填")
    @Schema(description = "收件日期")
    private Long collectionDate;
    @Schema(description = "发件部门")
    private String issuingDepartment;
    @Schema(description = "协调事项")
    private String coordination;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
