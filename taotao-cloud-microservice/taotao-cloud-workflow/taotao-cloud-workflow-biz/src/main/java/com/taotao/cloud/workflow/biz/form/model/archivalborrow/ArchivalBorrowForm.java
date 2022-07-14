package com.taotao.cloud.workflow.biz.form.model.archivalborrow;


import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 档案借阅申请
 *
 */
@Data
public class ArchivalBorrowForm {
    @NotNull(message = "紧急程度不能为空")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "申请原因")
    private String applyReason;
    @NotBlank(message = "档案编码不能为空")
    @Schema(description = "档案编码")
    private String archivesId;
    @NotBlank(message = "借阅方式不能为空")
    @Schema(description = "借阅方式")
    private String borrowMode;
    @NotBlank(message = "申请人员不能为空")
    @Schema(description = "申请人员")
    private String applyUser;
    @NotBlank(message = "流程标题不能为空")
    @Schema(description = "流程标题")
    private String flowTitle;
    @NotNull(message = "归还时间不能为空")
    @Schema(description = "归还时间")
    private Long returnDate;
    @NotBlank(message = "档案名称不能为空")
    @Schema(description = "档案名称")
    private String archivesName;
    @NotBlank(message = "借阅部门不能为空")
    @Schema(description = "借阅部门")
    private String borrowingDepartment;
    @NotNull(message = "借阅时间不能为空")
    @Schema(description = "借阅时间")
    private Long borrowingDate;
    @NotBlank(message = "档案属性不能为空")
    @Schema(description = "档案属性")
    private String archivalAttributes;
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
