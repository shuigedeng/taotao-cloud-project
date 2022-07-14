package com.taotao.cloud.workflow.biz.form.model.documentapproval;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 文件签批意见表
 *
 * @author JNPF开发平台组
 * @version V3.1.0
 * @copyright 引迈信息技术有限公司
 * @date 2021/3/15 8:46
 */
@Data
public class DocumentApprovalForm {
    @ApiModelProperty(value = "拟稿人")
    private String draftedPerson;
    @ApiModelProperty(value = "相关附件")
    private String fileJson;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程标题")
    private String flowTitle;
    @ApiModelProperty(value = "文件名称")
    private String fileName;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "紧急程度")
    private Integer flowUrgent;
    @ApiModelProperty(value = "发文单位")
    private String serviceUnit;
    @ApiModelProperty(value = "修改意见")
    private String modifyOpinion;
    @NotNull(message = "必填")
    @ApiModelProperty(value = "收文日期")
    private Long receiptDate;
    @ApiModelProperty(value = "文件拟办")
    private String fillPreparation;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程主键")
    private String flowId;
    @NotBlank(message = "必填")
    @ApiModelProperty(value = "流程单据")
    private String billNo;
    @ApiModelProperty(value = "文件编码")
    private String fillNum;
    @ApiModelProperty(value = "提交/保存 0-1")
    private String status;
    @ApiModelProperty(value = "候选人")
    private Map<String, List<String>> candidateList;
}
