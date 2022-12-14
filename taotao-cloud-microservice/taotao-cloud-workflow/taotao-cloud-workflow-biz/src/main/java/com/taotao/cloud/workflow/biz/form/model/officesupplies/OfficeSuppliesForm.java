package com.taotao.cloud.workflow.biz.form.model.officesupplies;


import java.util.List;
import java.util.Map;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 领用办公用品申请表
 */
@Data
public class OfficeSuppliesForm {
    @Schema(description = "用品名称")
    private String articlesName;
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "用品分类")
    private String classification;
    @Schema(description = "用品编码")
    private String articlesId;
    @Schema(description = "申请原因")
    private String applyReasons;
    @NotBlank(message = "申请人员不能为空")
    @Schema(description = "申请人员")
    private String applyUser;
    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "领用仓库")
    private String useStock;
    @Schema(description = "用品数量")
    private String articlesNum;
    @NotNull(message = "申请时间不能为空")
    @Schema(description = "申请时间")
    private Long  applyDate;
    @NotBlank(message = "所属部门不能为空")
    @Schema(description = "所属部门")
    private String department;
    @NotBlank(message = "流程主键不能为空")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;

}
