package com.taotao.cloud.workflow.api.common.model.form.finishedproduct;


import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 成品入库单
 *
 */
@Data
public class FinishedProductForm {
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "入库人")
    private String warehouseName;
    @Schema(description = "仓库")
    private String warehouse;
    @NotNull(message = "入库时间不能为空")
    @Schema(description = "入库时间")
    private Long  reservoirDate;
    @Schema(description = "备注")
    private String description;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "明细")
    List<FinishedProductEntryEntityInfoModel> entryList;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
