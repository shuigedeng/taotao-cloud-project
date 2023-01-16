package com.taotao.cloud.workflow.api.common.model.form.workcontactsheet;


import lombok.Data;

/**
 * 工作联系单
 *
 */
@Data
public class WorkContactSheetInfoVO {
    @Schema(description = "主键id")
    private String id;
    @Schema(description = "相关附件")
    private String fileJson;
    @Schema(description = "收件部门")
    private String serviceDepartment;
    @Schema(description = "流程标题")
    private String flowTitle;
    @Schema(description = "紧急程度")
    private Integer flowUrgent;
    @Schema(description = "收件人")
    private String recipients;
    @Schema(description = "发件日期")
    private Long toDate;
    @Schema(description = "发件人")
    private String drawPeople;
    @Schema(description = "流程主键")
    private String flowId;
    @Schema(description = "流程单据")
    private String billNo;
    @Schema(description = "收件日期")
    private Long collectionDate;
    @Schema(description = "发件部门")
    private String issuingDepartment;
    @Schema(description = "协调事项")
    private String coordination;

}
