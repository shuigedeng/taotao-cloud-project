package com.taotao.cloud.workflow.biz.form.model.materialrequisition;


import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 领料单
 */
@Data
public class MaterialRequisitionInfoVO {
    @Schema(description = "主键")
    private String id;
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
    @Schema(description = "明细")
    List<MaterialEntryEntityInfoModel> entryList;
}
