package com.taotao.cloud.workflow.biz.form.model.quotationapproval;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 报价审批表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class QuotationApprovalForm {
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @NotBlank(message = "流程标题不能为空")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @NotNull(message = "紧急程度不能为空")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "合作人名")
    private String partnerName;
    @NotNull(message = "填表日期不能为空")
    @ApiModelProperty(value = "填表日期")
    private Long  writeDate;
    @ApiModelProperty(value = "情况描述")
    private String custSituation;
    @ApiModelProperty(value = "填报人")
    private String writer;
    @NotBlank(message = "流程主键不能为空")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "流程单据不能为空")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "类型")
    private String quotationType;
    @ApiModelProperty(value = "客户名称")
    private String customerName;
    @ApiModelProperty(value = "模板参考")
    private String standardFile;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;

}
