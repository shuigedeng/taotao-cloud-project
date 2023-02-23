package com.taotao.cloud.workflow.biz.common.model.form.materialrequisition;


import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 领料单
 *
 */
@Data
public class MaterialRequisitionForm {
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @NotBlank(message = "必填")
    @Schema(description = "领料人")
    private String leadPeople;
    @NotBlank(message = "必填")
    @Schema(description = "领料部门")
    private String leadDepartment;
    @NotNull(message = "必填")
    @Schema(description = "领料日期")
    private Long leadDate;
    @Schema(description = "仓库")
    private String warehouse;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "明细")
    List<MaterialEntryEntityInfoModel> entryList;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
