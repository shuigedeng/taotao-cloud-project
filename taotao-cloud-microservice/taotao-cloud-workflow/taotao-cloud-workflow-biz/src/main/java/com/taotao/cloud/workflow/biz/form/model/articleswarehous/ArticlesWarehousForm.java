package com.taotao.cloud.workflow.biz.form.model.articleswarehous;


import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 用品入库申请表
 *
 */
@Data
public class ArticlesWarehousForm {
    @NotNull(message = "必填")
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "用品分类")
    private String classification;
    @Schema(description = "用品编码")
    private String articlesId;
    @Schema(description = "申请原因")
    private String applyReasons;
    @NotBlank(message = "必填")
    @Schema(description = "申请人员")
    private String applyUser;
    @NotBlank(message = "必填")
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "数量")
    private String estimatePeople;
    @Schema(description = "单位")
    private String company;
    @NotNull(message = "必填")
    @Schema(description = "申请时间")
    private Long applyDate;
    @NotBlank(message = "必填")
    @Schema(description = "所属部门")
    private String department;
    @NotBlank(message = "必填")
    @Schema(description = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "用品库存")
    private String articles;
    @Schema(description = "提交/保存 0-1")
    private String status;
    @Schema(description = "候选人")
    private Map<String, List<String>> candidateList;
}
