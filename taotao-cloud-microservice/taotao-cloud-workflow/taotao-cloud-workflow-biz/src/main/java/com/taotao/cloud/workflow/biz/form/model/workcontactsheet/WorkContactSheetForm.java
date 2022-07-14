package com.taotao.cloud.workflow.biz.form.model.workcontactsheet;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 工作联系单
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class WorkContactSheetForm {
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @ApiModelProperty(value = "收件部门")
    private String serviceDepartment;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "收件人")
    private String recipients;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "发件日期")
    private Long toDate;
    @ApiModelProperty(value = "发件人")
    private String drawPeople;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "收件日期")
    private Long collectionDate;
    @ApiModelProperty(value = "发件部门")
    private String issuingDepartment;
    @ApiModelProperty(value = "协调事项")
    private String coordination;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
